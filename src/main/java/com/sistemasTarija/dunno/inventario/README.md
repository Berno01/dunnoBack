# Módulo de Inventario - Documentación Técnica

## 📋 Descripción General

El módulo de **Inventario** proporciona endpoints especializados para la consulta y visualización estratégica del inventario de productos (modelos de ropa). Este módulo es completamente independiente de otros módulos del sistema y sigue la arquitectura hexagonal implementada en el proyecto.

## 🎯 Funcionalidades Principales

### 1. Listado General de Inventario

**Endpoint:** `GET /api/inventario`

Retorna un resumen de todos los modelos registrados con su **stock total global** (suma de todas las variantes en todas las sucursales).

**Response DTO:**

```java
InventarioResumenDTO {
    idModelo: Integer
    nombreModelo: String
    fotoPortada: String
    categoria: String
    marca: String
    corte: String
    totalStockGlobal: Long  // Calculado con SUM en BD
}
```

**Optimización SQL:**

- La query ejecuta `SUM(i.cantidad)` directamente en la base de datos
- No trae todas las filas a Java para sumar (eficiente)
- Usa proyección DTO para evitar carga de entidades completas

### 2. Detalle Matricial del Modelo

**Endpoint:** `GET /api/inventario/{idModelo}`

Retorna toda la información necesaria para construir una matriz **Color x Talla** con stocks por sucursal.

**Response DTO:**

```java
InventarioDetalleDTO {
    idModelo: Integer
    nombreModelo: String
    categoria: String
    marca: String
    corte: String
    coloresDisponibles: List<String>  // ["Azul", "Negro", "Gris"]
    tallasDisponibles: List<String>   // ["XS", "S", "M", "L", "XL"]
    sucursales: List<SucursalStockDTO>
}

SucursalStockDTO {
    idSucursal: Integer
    nombreSucursal: String
    matrizColorTalla: Map<String, Map<String, VarianteStockDTO>>
    // Ejemplo: {"Azul": {"S": {idVariante:1, stock:5}, "M": {idVariante:2, stock:3}}}
}

VarianteStockDTO {
    idVariante: Integer
    nombreTalla: String
    nombreColor: String
    codigoHexColor: String
    stock: Integer  // 0 si no existe registro en inventario
}
```

**Características:**

- Incluye datos de todas las sucursales activas
- Agrega una sucursal especial "Global" (id=0) con la suma total de stocks
- Garantiza que todas las celdas de la matriz tengan valor (0 si no hay stock)
- Frontend puede filtrar/cambiar tabs sin necesidad de recargar datos

## 🏗️ Arquitectura

El módulo sigue **Arquitectura Hexagonal** (Ports & Adapters):

```
inventario/
├── domain/
│   ├── model/           # Entidades de dominio puras
│   │   ├── Modelo.java
│   │   ├── ModeloColor.java
│   │   ├── Variante.java
│   │   ├── Inventario.java
│   │   ├── Sucursal.java
│   │   └── options/     # Marca, Categoria, Corte, Color, Talla
│   └── exception/       # Excepciones de dominio
│       └── InventarioNotFoundException.java
│
├── application/
│   ├── dto/             # DTOs de respuesta
│   │   ├── InventarioResumenDTO.java
│   │   ├── InventarioDetalleDTO.java
│   │   ├── SucursalStockDTO.java
│   │   └── VarianteStockDTO.java
│   ├── port/
│   │   ├── in/          # Casos de uso (interfaces)
│   │   │   └── ConsultarInventarioUseCase.java
│   │   └── out/         # Puertos de persistencia (interfaces)
│   │       └── InventarioPersistencePort.java
│   └── service/         # Implementación de casos de uso
│       └── InventarioService.java
│
└── infrastructure/
    └── adapter/
        ├── in/
        │   └── web/     # Controladores REST
        │       └── InventarioController.java
        └── out/
            └── persistence/
                ├── entity/  # Entidades JPA
                │   ├── InventarioEntity.java
                │   ├── SucursalEntity.java
                │   └── modelo/  # ModeloInventarioEntity, etc.
                ├── repository/  # Repositorios JPA
                │   ├── InventarioRepository.java
                │   ├── ModeloInventarioRepository.java
                │   └── ...
                ├── mapper/  # Mappers entity ↔ domain
                │   ├── InventarioPersistenceMapper.java
                │   ├── ModeloPersistenceMapper.java
                │   └── SucursalPersistenceMapper.java
                └── InventarioRepositoryAdapter.java
```

## 🔧 Componentes Clave

### 1. InventarioService

Lógica de negocio principal:

- `obtenerListadoGeneral()`: Delega la query optimizada al puerto de persistencia
- `obtenerDetalleModelo()`: Construye la matriz Color x Talla
  - Carga el modelo con todos sus colores y variantes
  - Agrupa inventarios por sucursal
  - Crea matrices para cada sucursal
  - Calcula el "Global" sumando todos los stocks

### 2. InventarioRepository (Query Optimizada)

```sql
SELECT
    m.id, m.nombre, MIN(mc.fotoUrl), cat.nombre, ma.nombre, co.nombre,
    COALESCE(SUM(i.stockInventario), 0)
FROM modelo m
LEFT JOIN marca ma ON ...
LEFT JOIN categoria cat ON ...
LEFT JOIN corte co ON ...
LEFT JOIN modelo_color mc ON mc.id_modelo = m.id
LEFT JOIN variante v ON v.id_modelo_color = mc.id
LEFT JOIN inventario i ON i.id_variante = v.id AND i.estado = true
WHERE m.estado = true
GROUP BY m.id, m.nombre, cat.nombre, ma.nombre, co.nombre
```

**Ventajas:**

- Evita N+1 queries
- Cálculo de suma en BD (no en Java)
- Uso de LEFT JOIN para incluir modelos sin stock
- COALESCE para evitar NULL en totalStockGlobal

### 3. InventarioRepositoryAdapter

Implementa el puerto de persistencia:

- Coordina múltiples repositorios JPA
- Mapea entidades ↔ dominio
- Carga eager/lazy optimizada con FETCH JOIN
- Evita N+1 queries con consultas estratégicas

## 📊 Flujo de Datos

### Endpoint 1: Listado General

```
Cliente → Controller → UseCase → PersistencePort → Repository
                                                    ↓ (Query SQL optimizada)
                                                   BD
                                                    ↓
Cliente ← InventarioResumenDTO[] ←←←←←←←←←←←←←←←← List<InventarioResumenDTO>
```

### Endpoint 2: Detalle Matricial

```
Cliente → Controller → UseCase → PersistencePort
                         ↓
                    Service procesa:
                    1. findModeloById() → carga modelo + colores + variantes
                    2. findInventariosByModelo() → todos los registros de inventario
                    3. findAllSucursales() → sucursales activas
                    4. Construcción de matrices en Java
                    5. Agregación "Global"
                         ↓
Cliente ← InventarioDetalleDTO (con matrices completas)
```

## 🎨 Uso del Frontend

### Renderizado de la Matriz

```javascript
// El frontend recibe InventarioDetalleDTO
{
  "nombreModelo": "Remera Deportiva",
  "coloresDisponibles": ["Azul", "Negro"],
  "tallasDisponibles": ["S", "M", "L"],
  "sucursales": [
    {
      "idSucursal": 1,
      "nombreSucursal": "Tarija",
      "matrizColorTalla": {
        "Azul": {
          "S": { "idVariante": 1, "stock": 5 },
          "M": { "idVariante": 2, "stock": 3 },
          "L": { "idVariante": 3, "stock": 0 }
        },
        "Negro": { ... }
      }
    },
    {
      "idSucursal": 2,
      "nombreSucursal": "Cochabamba",
      ...
    },
    {
      "idSucursal": 0,
      "nombreSucursal": "Global",
      // Suma de todas las sucursales
    }
  ]
}
```

El frontend puede:

1. Crear tabs por sucursal (Tarija, Cochabamba, La Paz, Global)
2. Renderizar tabla con:
   - Filas: Colores
   - Columnas: Tallas
   - Celdas: Stock (con idVariante para acciones)
3. Cambiar entre sucursales sin recargar (todos los datos ya están)

## 🔒 Independencia del Módulo

**Importante:** Este módulo NO depende de `catalogo`, `recepcion` ni `venta`.

Tiene sus propias copias de:

- Modelo, ModeloColor, Variante
- Marca, Categoria, Corte, Color, Talla
- Inventario, Sucursal

Esto garantiza:

- ✅ Desacoplamiento total entre módulos
- ✅ Evolución independiente
- ✅ Sin dependencias circulares
- ✅ Facilita testing y mantenimiento

## 🚀 Próximos Pasos

Para usar el módulo:

1. **Backend está listo** - Todos los archivos creados
2. **Verificar base de datos:**

   - Tablas: `modelo`, `modelo_color`, `variante`, `inventario`, `sucursal`
   - Tablas de opciones: `marca`, `categoria`, `corte`, `color`, `talla`

3. **Testing:**

   ```bash
   # Listado general
   GET http://localhost:8080/api/inventario

   # Detalle de un modelo específico
   GET http://localhost:8080/api/inventario/1
   ```

4. **Frontend:**
   - Consumir `/api/inventario` para mostrar lista de modelos
   - Consumir `/api/inventario/{id}` para mostrar matriz detallada
   - Implementar filtros/tabs por sucursal del lado cliente

## 📝 Notas Técnicas

- **Stock en 0 vs NULL:** El servicio garantiza que siempre devuelva stock=0 si no existe registro, nunca NULL
- **Fetch Strategy:** Configurado estratégicamente (EAGER/LAZY) para optimizar queries
- **Transaction Management:** No requiere @Transactional en lecturas (queries optimizadas)
- **Exception Handling:** Lanza `InventarioNotFoundException` si el modelo no existe

---

**Desarrollado siguiendo los patrones del proyecto DUNNO**

- Arquitectura Hexagonal
- Clean Code
- Optimización SQL
- Independencia de módulos

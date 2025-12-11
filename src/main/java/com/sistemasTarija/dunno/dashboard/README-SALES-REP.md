# Dashboard de Análisis de Vendedores

## 📊 Descripción

Módulo de backend para el dashboard analítico enfocado en el rendimiento de vendedores. Proporciona métricas, rankings y análisis de ventas con filtrado dinámico avanzado.

## 🏗️ Arquitectura

Sigue arquitectura hexagonal (ports & adapters):

```
dashboard/
├── application/
│   ├── dto/                              # DTOs de transferencia
│   │   ├── DashboardSalesRepResponseDTO  # Respuesta principal
│   │   ├── RankingVendedorDTO            # Ranking de vendedores
│   │   ├── AnalisisDescuentosDTO         # Métricas de descuentos
│   │   ├── TopItemDTO                    # Items genéricos (categorías, modelos, etc.)
│   │   └── DashboardFilterDTO            # Filtros de entrada
│   ├── port/
│   │   ├── in/
│   │   │   └── GetDashboardSalesRepUseCase  # Caso de uso principal
│   │   └── out/
│   │       └── DashboardPersistencePort     # Port de persistencia
│   └── service/
│       └── DashboardSalesRepService         # Implementación del caso de uso
└── infrastructure/
    └── adapter/
        ├── in/web/controller/
        │   └── DashboardSalesRepController  # REST Controller
        └── out/persistence/
            ├── DashboardPersistenceAdapter  # Implementación del port
            ├── repository/
            │   ├── DashboardVentaRepository    # Queries optimizadas
            │   └── DashboardUsuarioRepository  # Datos de usuarios
            └── specification/
                └── VentaDashboardSpecification # Filtros dinámicos (no usado actualmente)
```

## 🎯 Funcionalidades

### Secciones del Dashboard

1. **Ranking de Vendedores**
   - Lista ordenada por total vendido
   - Incluye: nombre, monto total, cantidad de ventas
   - Posición en el ranking

2. **Análisis de Descuentos**
   - Total descontado (Bs.)
   - Cantidad de descuentos aplicados
   - Promedio por descuento
   - Porcentaje sobre ventas brutas

3. **Top Categorías**
   - Top 5 categorías más vendidas
   - Cantidad de unidades y porcentaje

4. **Top Modelos**
   - Top 5 modelos más vendidos
   - Cantidad de unidades y porcentaje

5. **Top Colores**
   - Top 5 colores más vendidos
   - Incluye código hexadecimal del color
   - Cantidad de unidades y porcentaje

6. **Distribución de Tallas**
   - Top 10 tallas vendidas
   - Cantidad de unidades y porcentaje

## 🔍 Filtrado Dinámico

### Filtros Disponibles

- **startDate** (LocalDate): Fecha de inicio del rango
- **endDate** (LocalDate): Fecha de fin del rango
- **salesRepId** (Integer): ID del vendedor específico
- **idSucursal** (Integer): ID de sucursal (para usuarios no admin)

### Reglas de Filtrado

#### Filtro Global Obligatorio
- ✅ Solo ventas con `estado_venta = true`

#### Filtro de Fechas
- Si se proveen ambas fechas: filtra por rango
- Si solo hay startDate: desde esa fecha en adelante
- Si solo hay endDate: hasta esa fecha
- Si no hay fechas: todos los datos históricos

#### Filtro Interactivo de Vendedor (Crítico)

**Caso 1: salesRepId = null**
- Ranking: Muestra TODOS los vendedores
- Otras secciones: Datos agregados de TODA la empresa

**Caso 2: salesRepId != null** (usuario hizo clic en un vendedor)
- Ranking: Puede mostrar todos o resaltar al seleccionado
- Otras secciones: SOLO datos de ese vendedor específico

## 🚀 API Endpoints

### GET /api/dashboard/sales-rep-analysis

Obtiene todos los datos del dashboard.

**Query Parameters:**
- `startDate` (opcional): Fecha inicio en formato `yyyy-MM-dd`
- `endDate` (opcional): Fecha fin en formato `yyyy-MM-dd`
- `salesRepId` (opcional): ID del vendedor

**Headers:**
- `X-Usuario-Id`: ID del usuario autenticado

**Ejemplo de Request:**
```http
GET /api/dashboard/sales-rep-analysis?startDate=2025-01-01&endDate=2025-12-31&salesRepId=5
X-Usuario-Id: 123
```

**Ejemplo de Response:**
```json
{
  "rankingVendedores": [
    {
      "idUsuario": 5,
      "nombreCompleto": "Carlos Mendoza",
      "username": "Tarija",
      "totalVendido": 45890.0,
      "cantidadVentas": 187,
      "posicion": 1
    },
    {
      "idUsuario": 8,
      "nombreCompleto": "María Fernández",
      "username": "Tarija",
      "totalVendido": 38750.0,
      "cantidadVentas": 162,
      "posicion": 2
    }
  ],
  "analisisDescuentos": {
    "totalDescontado": 7870.0,
    "cantidadDescuentos": 80,
    "promedioPorDescuento": 98.38,
    "porcentajeSobreVentasBrutas": 6.5
  },
  "topCategorias": [
    {
      "id": 1,
      "nombre": "Poleras",
      "cantidad": 219,
      "porcentaje": 35.2,
      "posicion": 1
    },
    {
      "id": 2,
      "nombre": "Pantalones",
      "cantidad": 163,
      "porcentaje": 26.2,
      "posicion": 2
    }
  ],
  "topModelos": [
    {
      "id": 10,
      "nombre": "Básica Cotton",
      "cantidad": 113,
      "porcentaje": 18.2,
      "posicion": 1
    }
  ],
  "topColores": [
    {
      "id": 3,
      "nombre": "Negro",
      "cantidad": 174,
      "porcentaje": 31.0,
      "posicion": 1,
      "codigoHex": "#000000"
    }
  ],
  "distribucionTallas": [
    {
      "id": 3,
      "nombre": "M",
      "cantidad": 182,
      "porcentaje": 30.0,
      "posicion": 1
    }
  ]
}
```

### POST /api/dashboard/sales-rep-analysis

Variante que acepta filtros en el body (para filtros más complejos en el futuro).

**Request Body:**
```json
{
  "startDate": "2025-01-01",
  "endDate": "2025-12-31",
  "salesRepId": 5,
  "idSucursal": 1
}
```

## ⚡ Optimización de Rendimiento

### Estrategia Implementada

1. **Una Query por Sección**: Cada sección ejecuta UNA sola query optimizada
2. **Agregaciones en DB**: Usa `SUM`, `COUNT`, `GROUP BY` directamente en la base de datos
3. **Filtrado a Nivel de Query**: Los filtros se aplican en el WHERE, no en memoria
4. **JOINs Optimizados**: Solo se hacen los JOINs necesarios para cada métrica
5. **Fetch de Usuarios en Batch**: Los datos de usuarios se cargan en una sola query adicional

### Queries Ejecutadas

Para obtener el dashboard completo:
- ✅ 1 query: Ranking de vendedores + agregación
- ✅ 1 query: Análisis de descuentos
- ✅ 1 query: Top categorías
- ✅ 1 query: Top modelos
- ✅ 1 query: Top colores
- ✅ 1 query: Distribución de tallas
- ✅ 1 query: Datos de usuarios (batch)

**Total: ~7 queries** (independiente del número de vendedores o cambios de filtro)

### Ventajas vs Enfoque Ingenuo

❌ **Enfoque Ingenuo**: 7 queries base + 7 queries más cada vez que cambia el filtro = 14+ queries

✅ **Enfoque Optimizado**: Siempre 7 queries, sin importar los filtros aplicados

## 🔧 Configuración

### Dependencias

El módulo usa las mismas dependencias que el resto del proyecto:
- Spring Boot
- Spring Data JPA
- Hibernate
- Lombok
- PostgreSQL (o tu base de datos)

### Base de Datos

El módulo lee de las siguientes tablas existentes:
- `venta`
- `detalle_venta`
- `usuario`
- `variante`
- `modelo_color`
- `modelo`
- `color`
- `categoria`
- `talla`

No requiere tablas adicionales.

## 🧪 Testing

### Pruebas Recomendadas

1. **Sin filtros**: Todos los datos históricos
   ```
   GET /api/dashboard/sales-rep-analysis
   ```

2. **Con rango de fechas**:
   ```
   GET /api/dashboard/sales-rep-analysis?startDate=2025-01-01&endDate=2025-01-31
   ```

3. **Filtrado por vendedor específico**:
   ```
   GET /api/dashboard/sales-rep-analysis?salesRepId=5
   ```

4. **Combinación de filtros**:
   ```
   GET /api/dashboard/sales-rep-analysis?startDate=2025-01-01&endDate=2025-12-31&salesRepId=5
   ```

## 📝 Notas Importantes

### TODOs Pendientes

1. **Filtro de Sucursal**: El controller tiene un TODO para implementar el filtro automático de sucursal para usuarios no admin. Debes:
   - Inyectar un servicio de usuario
   - Verificar si el usuario es admin
   - Si no es admin, setear `filter.setIdSucursal(usuario.getIdSucursal())`

2. **Autenticación**: Asegurarse de que el header `X-Usuario-Id` esté siendo validado

### Extensibilidad

El diseño permite agregar fácilmente:
- Más filtros (marca, categoría, etc.)
- Más secciones al dashboard
- Diferentes límites para los tops
- Exportación a PDF/Excel
- Caching de resultados

### Specifications (Opcional)

Se creó `VentaDashboardSpecification` pero actualmente no se usa porque las queries JPQL son más eficientes. Sin embargo, está disponible si en el futuro necesitas:
- Filtros más complejos y dinámicos
- Combinaciones de filtros que no se conocen en tiempo de compilación

## 🎨 Frontend - Integración

### Flujo de Usuario

1. **Carga inicial**: El frontend llama al endpoint sin `salesRepId`
   - Muestra el ranking completo de vendedores
   - Muestra métricas globales de la empresa

2. **Click en vendedor**: El usuario hace clic en un vendedor del ranking
   - El frontend llama al endpoint con `salesRepId=<id>`
   - Las secciones 2-6 se actualizan mostrando solo datos de ese vendedor
   - El ranking puede permanecer igual o resaltar al vendedor seleccionado

3. **Cambio de fechas**: El usuario modifica el rango de fechas
   - El frontend llama al endpoint con los nuevos `startDate` y `endDate`
   - Mantiene el `salesRepId` si había uno seleccionado
   - Todas las secciones se actualizan con el nuevo rango

### Ejemplo de Integración React/Angular

```typescript
// Estado inicial
const [filters, setFilters] = useState({
  startDate: '2025-01-01',
  endDate: '2025-12-31',
  salesRepId: null
});

// Función para cargar dashboard
const loadDashboard = async () => {
  const params = new URLSearchParams();
  if (filters.startDate) params.append('startDate', filters.startDate);
  if (filters.endDate) params.append('endDate', filters.endDate);
  if (filters.salesRepId) params.append('salesRepId', filters.salesRepId.toString());
  
  const response = await fetch(
    `/api/dashboard/sales-rep-analysis?${params}`,
    {
      headers: { 'X-Usuario-Id': currentUser.id }
    }
  );
  
  return await response.json();
};

// Handler cuando se hace clic en un vendedor
const onSalesRepClick = (salesRepId) => {
  setFilters({ ...filters, salesRepId });
  // loadDashboard() se ejecutará automáticamente por useEffect
};
```

## 🐛 Troubleshooting

### Problema: No se muestran datos
- Verificar que existan ventas con `estado_venta = true`
- Verificar el rango de fechas
- Revisar que el usuario tenga permisos a la sucursal

### Problema: Porcentajes no suman 100%
- Es normal si se aplica un límite (top 5, top 10)
- Los porcentajes son sobre el total, no sobre el subconjunto mostrado

### Problema: Queries lentas
- Revisar índices en: `estado_venta`, `fecha`, `created_by`, `id_sucursal`
- Considerar agregar índices compuestos
- Verificar el plan de ejecución con `EXPLAIN ANALYZE`

## 📚 Referencias

- Patrón Port & Adapters: https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/
- Spring Data JPA Specifications: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#specifications
- JPQL Query Optimization: https://thorben-janssen.com/jpql/

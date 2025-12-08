# Módulo Dashboard

## Descripción

Módulo de Analytics & Reporting que proporciona 6 endpoints de solo lectura (GET) para visualización de métricas de ventas.

## Estructura del Módulo

```
dashboard/
├── application/
│   ├── dto/
│   │   ├── DashboardKPIsDTO.java
│   │   ├── VentasPorHoraDTO.java
│   │   ├── VentasPorCategoriaDTO.java
│   │   ├── MetodosPagoDTO.java
│   │   ├── DistribucionTallasDTO.java
│   │   └── TopProductoDTO.java
│   └── service/
│       └── DashboardService.java
└── infrastructure/
    └── adapter/
        ├── in/
        │   └── web/
        │       └── controller/
        │           └── DashboardController.java
        └── out/
            └── persistence/
                ├── entity/
                │   ├── VentaEntity.java
                │   ├── DetalleVentaEntity.java
                │   ├── InventarioEntity.java
                │   └── modelo/
                │       ├── VarianteEntity.java
                │       ├── ModeloColorEntity.java
                │       ├── ModeloEntity.java
                │       ├── TallaEntity.java
                │       ├── CategoriaEntity.java
                │       ├── MarcaEntity.java
                │       ├── ColorEntity.java
                │       └── CorteEntity.java
                └── repository/
                    └── DashboardRepository.java
```

## Endpoints Disponibles

### 1. GET /api/dashboard/kpis

**Descripción:** Retorna los KPIs principales del dashboard.

**Parámetros opcionales:**

- `idSucursal` (Long): ID de la sucursal. Si es NULL, retorna datos globales.
- `fechaInicio` (LocalDate): Fecha de inicio del filtro. Si es NULL, usa hoy.
- `fechaFin` (LocalDate): Fecha de fin del filtro. Si es NULL, usa hoy.

**Respuesta:**

```json
{
  "totalVentas": 15000.0,
  "cantidadVentas": 50,
  "ticketPromedio": 300.0,
  "unidadesVendidas": 120
}
```

---

### 2. GET /api/dashboard/ventas-por-hora

**Descripción:** Retorna las ventas agrupadas por hora del día.

**Parámetros opcionales:**

- `idSucursal` (Long)
- `fechaInicio` (LocalDate)
- `fechaFin` (LocalDate)

**Respuesta:**

```json
[
  { "hora": 9, "total": 1200.0 },
  { "hora": 10, "total": 2500.0 },
  { "hora": 14, "total": 3800.0 }
]
```

---

### 3. GET /api/dashboard/ventas-por-categoria

**Descripción:** Retorna las ventas agrupadas por categoría (ordenado descendente).

**Parámetros opcionales:**

- `idSucursal` (Long)
- `fechaInicio` (LocalDate)
- `fechaFin` (LocalDate)

**Respuesta:**

```json
[
  { "categoria": "Poleras", "cantidad": 80 },
  { "categoria": "Pantalones", "cantidad": 45 },
  { "categoria": "Casacas", "cantidad": 30 }
]
```

---

### 4. GET /api/dashboard/metodos-pago

**Descripción:** Retorna los métodos de pago utilizados.

**Parámetros opcionales:**

- `idSucursal` (Long)
- `fechaInicio` (LocalDate)
- `fechaFin` (LocalDate)

**Respuesta:**

```json
[
  { "metodo": "Efectivo", "cantidad": 30 },
  { "metodo": "QR", "cantidad": 15 },
  { "metodo": "Tarjeta", "cantidad": 10 }
]
```

---

### 5. GET /api/dashboard/distribucion-tallas

**Descripción:** Retorna la distribución de tallas vendidas.

**Parámetros opcionales:**

- `idSucursal` (Long)
- `fechaInicio` (LocalDate)
- `fechaFin` (LocalDate)

**Respuesta:**

```json
[
  { "talla": "M", "cantidad": 50 },
  { "talla": "L", "cantidad": 35 },
  { "talla": "S", "cantidad": 25 }
]
```

---

### 6. GET /api/dashboard/top-productos ⭐ CRÍTICO

**Descripción:** Retorna los productos más vendidos con stock actual.

**Parámetros opcionales:**

- `idSucursal` (Long)
- `fechaInicio` (LocalDate)
- `fechaFin` (LocalDate)
- `limit` (int): Número de productos a retornar (default: 5)

**Respuesta:**

```json
[
  {
    "nombreModelo": "Polo Básico",
    "subtitulo": "Poleras - Nike",
    "cantidadVendida": 45,
    "stockActual": 120,
    "fotoUrl": "https://example.com/foto.jpg"
  },
  {
    "nombreModelo": "Jean Slim Fit",
    "subtitulo": "Pantalones - Levi's",
    "cantidadVendida": 38,
    "stockActual": 85,
    "fotoUrl": "https://example.com/foto2.jpg"
  }
]
```

## Reglas de Negocio

1. **Filtro por Sucursal:**

   - Si `idSucursal` es NULL → Retorna datos globales (SUM de todas las sucursales)
   - Si `idSucursal` tiene valor → Retorna datos solo de esa sucursal

2. **Filtro por Fechas:**

   - Si `fechaInicio` y `fechaFin` son NULL → Usa `LocalDate.now()` (Hoy)
   - Las consultas filtran por rango: `fecha BETWEEN fechaInicio AND fechaFin`

3. **Ventas Activas:**

   - Todos los endpoints filtran por `estado = true` (ventas activas)

4. **Manejo de NULL:**
   - Todas las queries usan `COALESCE` para evitar valores NULL en el JSON
   - Si no hay datos, retorna 0 o listas vacías

## Queries Optimizadas

- Todas las queries están optimizadas con JOINs eficientes
- No se traen objetos enteros a memoria
- Se usa `COALESCE` para manejar valores NULL
- Queries nativas donde es necesario (métodos de pago, top productos)
- Stock calculado en tiempo real desde la tabla `inventario`

## Tecnologías Utilizadas

- Spring Boot
- Spring Data JPA
- JPQL y SQL Nativo
- Lombok
- Jakarta Persistence

## Notas Importantes

- Este módulo es **independiente** y NO usa código de otros módulos (venta, catalogo, inventario)
- Todas las entidades son copias locales para mantener la separación de módulos
- Todos los endpoints son de **solo lectura** (GET)
- El stock actual se calcula sumando el inventario disponible por modelo

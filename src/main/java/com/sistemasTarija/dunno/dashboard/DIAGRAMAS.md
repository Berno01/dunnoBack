# 📐 Diagramas y Arquitectura Visual

## 🏗️ Diagrama de Arquitectura Hexagonal

```
┌─────────────────────────────────────────────────────────────────────┐
│                         FRONTEND (React/Vue/Angular)                 │
│                                                                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐              │
│  │   Ranking    │  │  Descuentos  │  │  Top Items   │              │
│  │  Component   │  │  Component   │  │  Component   │              │
│  └──────────────┘  └──────────────┘  └──────────────┘              │
└───────────────────────────┬─────────────────────────────────────────┘
                            │ HTTP REST
                            │ GET /api/dashboard/sales-rep-analysis
                            ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    INFRASTRUCTURE LAYER (IN)                         │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │         DashboardSalesRepController                            │ │
│  │  - Recibe HTTP requests                                        │ │
│  │  - Valida parámetros                                           │ │
│  │  - Construye DashboardFilterDTO                                │ │
│  │  - Retorna ResponseEntity<DashboardSalesRepResponseDTO>        │ │
│  └────────────────────────────────────────────────────────────────┘ │
└───────────────────────────┬─────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────────┐
│                       APPLICATION LAYER                              │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │              GetDashboardSalesRepUseCase (Interface)           │ │
│  │  + getDashboardData(DashboardFilterDTO): ResponseDTO           │ │
│  └────────────────────────────────────────────────────────────────┘ │
│                            ▲                                         │
│                            │ implements                              │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │           DashboardSalesRepService                             │ │
│  │  - Coordina las 6 secciones del dashboard                      │ │
│  │  - Aplica límites (top 5, top 10)                              │ │
│  │  - Logging                                                      │ │
│  │  - Construye el DTO de respuesta completo                      │ │
│  └────────────────────────────────────────────────────────────────┘ │
└───────────────────────────┬─────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────────┐
│                       APPLICATION LAYER (PORT)                       │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │           DashboardPersistencePort (Interface)                 │ │
│  │  + getRankingVendedores(filter): List<RankingVendedorDTO>     │ │
│  │  + getAnalisisDescuentos(filter): AnalisisDescuentosDTO        │ │
│  │  + getTopCategorias(filter, limit): List<TopItemDTO>           │ │
│  │  + getTopModelos(filter, limit): List<TopItemDTO>              │ │
│  │  + getTopColores(filter, limit): List<TopItemDTO>              │ │
│  │  + getDistribucionTallas(filter, limit): List<TopItemDTO>      │ │
│  └────────────────────────────────────────────────────────────────┘ │
└───────────────────────────┬─────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    INFRASTRUCTURE LAYER (OUT)                        │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │            DashboardPersistenceAdapter                         │ │
│  │  - Implementa DashboardPersistencePort                         │ │
│  │  - Mapea Object[] a DTOs                                       │ │
│  │  - Calcula porcentajes                                         │ │
│  │  - Carga usuarios en batch                                     │ │
│  └────────┬───────────────────────────────────────────┬───────────┘ │
│           │                                           │              │
│           ▼                                           ▼              │
│  ┌──────────────────────────┐         ┌──────────────────────────┐ │
│  │ DashboardVentaRepository │         │ DashboardUsuarioRepository│ │
│  │ (JpaRepository)          │         │ (JpaRepository)           │ │
│  │ - 6 @Query methods       │         │ - findByIdIn()            │ │
│  │ - JPQL optimizado        │         │                           │ │
│  └──────────────────────────┘         └──────────────────────────┘ │
└───────────────────────────┬─────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      DATABASE (PostgreSQL)                           │
│                                                                       │
│  venta ──┬── detalle_venta ──┬── variante ──┬── modelo_color ──┬─ modelo    │
│          │                    │              │                  │   └─ categoria │
│          │                    │              │                  └─ color       │
│          │                    │              └─ talla                          │
│          └── usuario                                                          │
└─────────────────────────────────────────────────────────────────────┘
```

## 🔄 Flujo de Datos Detallado

### Request Flow (Frontend → Database)

```
1. User Action (Frontend)
   └─→ "Click en Vendedor Carlos (id=5)"

2. HTTP Request
   └─→ GET /api/dashboard/sales-rep-analysis?salesRepId=5
       Header: X-Usuario-Id: 1

3. Controller Layer
   ├─→ Recibe request
   ├─→ Valida parámetros
   ├─→ Construye DashboardFilterDTO {
   │      startDate: null,
   │      endDate: null,
   │      salesRepId: 5,
   │      idSucursal: null
   │   }
   └─→ Llama a Service

4. Service Layer
   ├─→ Llama a Port: getRankingVendedores(filter)
   ├─→ Llama a Port: getAnalisisDescuentos(filter)
   ├─→ Llama a Port: getTopCategorias(filter, 5)
   ├─→ Llama a Port: getTopModelos(filter, 5)
   ├─→ Llama a Port: getTopColores(filter, 5)
   ├─→ Llama a Port: getDistribucionTallas(filter, 10)
   └─→ Construye DashboardSalesRepResponseDTO

5. Adapter Layer
   ├─→ Convierte filtros a parámetros SQL
   ├─→ Llama a Repository methods
   ├─→ Mapea Object[] a DTOs
   ├─→ Calcula porcentajes
   └─→ Retorna DTOs

6. Repository Layer
   ├─→ Query 1: SELECT v.created_by, SUM(v.total), COUNT(v) FROM venta...
   ├─→ Query 2: SELECT SUM(v.descuento), COUNT(...), SUM(...) FROM venta...
   ├─→ Query 3: SELECT cat.id, cat.nombre, SUM(dv.cantidad) FROM venta...
   ├─→ Query 4: SELECT m.id, m.nombre, SUM(dv.cantidad) FROM venta...
   ├─→ Query 5: SELECT c.id, c.nombre, c.hex, SUM(dv.cantidad) FROM venta...
   ├─→ Query 6: SELECT t.id, t.nombre, SUM(dv.cantidad) FROM venta...
   └─→ Query 7: SELECT u.id, u.nombre FROM usuario WHERE u.id IN (...)

7. Database
   └─→ Ejecuta queries con índices optimizados
```

### Response Flow (Database → Frontend)

```
7. Database Results
   └─→ Retorna resultados de 7 queries

6. Repository
   └─→ Retorna List<Object[]> o Object[]

5. Adapter
   ├─→ Mapea Object[] a RankingVendedorDTO
   ├─→ Mapea Object[] a AnalisisDescuentosDTO
   ├─→ Mapea Object[] a TopItemDTO (múltiples listas)
   └─→ Retorna DTOs al Service

4. Service
   ├─→ Recibe todos los DTOs
   ├─→ Construye DashboardSalesRepResponseDTO {
   │      rankingVendedores: [...],
   │      analisisDescuentos: {...},
   │      topCategorias: [...],
   │      topModelos: [...],
   │      topColores: [...],
   │      distribucionTallas: [...]
   │   }
   └─→ Retorna al Controller

3. Controller
   └─→ Retorna ResponseEntity.ok(response)

2. HTTP Response
   └─→ Status: 200 OK
       Content-Type: application/json
       Body: { ... dashboard data ... }

1. Frontend
   └─→ Actualiza UI con los datos recibidos
```

## 📊 Diagrama de Queries SQL

```
DashboardVentaRepository
    │
    ├─→ getRankingVendedores()
    │       │
    │       └─→ SELECT v.created_by, SUM(v.total), COUNT(v)
    │           FROM venta v
    │           WHERE estado_venta = true
    │           AND (filtros...)
    │           GROUP BY v.created_by
    │           ORDER BY SUM(v.total) DESC
    │
    ├─→ getAnalisisDescuentos()
    │       │
    │       └─→ SELECT SUM(v.descuento), 
    │                  COUNT(CASE WHEN v.descuento > 0...),
    │                  SUM(v.total + v.descuento)
    │           FROM venta v
    │           WHERE estado_venta = true
    │           AND (filtros...)
    │
    ├─→ getTopCategorias()
    │       │
    │       └─→ SELECT cat.id, cat.nombre, SUM(dv.cantidad)
    │           FROM venta v
    │           JOIN detalle_venta dv ...
    │           JOIN variante var ...
    │           JOIN modelo_color mc ...
    │           JOIN modelo m ...
    │           JOIN categoria cat ...
    │           WHERE v.estado_venta = true
    │           AND (filtros...)
    │           GROUP BY cat.id, cat.nombre
    │           ORDER BY SUM(dv.cantidad) DESC
    │           LIMIT 5
    │
    ├─→ getTopModelos()
    │       │
    │       └─→ Similar a categorías pero GROUP BY modelo
    │
    ├─→ getTopColores()
    │       │
    │       └─→ Similar pero JOIN con color y GROUP BY color
    │
    └─→ getDistribucionTallas()
            │
            └─→ Similar pero JOIN con talla y GROUP BY talla

DashboardUsuarioRepository
    │
    └─→ findByIdIn(List<Integer> ids)
            │
            └─→ SELECT u.* FROM usuario u
                WHERE u.id_usuario IN (5, 8, 12, ...)
```

## 🎯 Diagrama de Filtrado Dinámico

```
                        DashboardFilterDTO
                                │
                    ┌───────────┼───────────┐
                    │           │           │
                    ▼           ▼           ▼
            startDate?    endDate?    salesRepId?
                │           │           │
        ┌───────┴───────┐   │   ┌───────┴───────┐
        ▼               ▼   ▼   ▼               ▼
      null           '2025-01-01'             null
        │               │   │   │               │
        │               │   │   │               │
        ▼               ▼   ▼   ▼               ▼
    No filtrar      WHERE        No filtrar
      por fecha     fecha >= X   por vendedor
                    AND 
                    fecha <= Y

                Todos los filtros se combinan con AND

WHERE v.estado_venta = true
  AND (:startDate IS NULL OR v.fecha >= :startDate)
  AND (:endDate IS NULL OR v.fecha <= :endDate)
  AND (:salesRepId IS NULL OR v.created_by = :salesRepId)
  AND (:idSucursal IS NULL OR v.id_sucursal = :idSucursal)

    Si el parámetro es NULL → Condición se evalúa como TRUE
    Si el parámetro tiene valor → Se aplica el filtro
```

## 🎨 Diagrama de Mapeo Object[] → DTO

```
Query Result (Object[])                     TopItemDTO
┌─────────────────────┐                  ┌──────────────────┐
│ [0] Integer id      │ ──────────────→  │ id: Integer      │
│ [1] String nombre   │ ──────────────→  │ nombre: String   │
│ [2] Long cantidad   │ ──────────────→  │ cantidad: Long   │
│                     │                  │ porcentaje: ?    │ ← Calculado
│                     │                  │ posicion: ?      │ ← Calculado
└─────────────────────┘                  └──────────────────┘

Cálculo de porcentaje:
    totalUnidades = SUM(todas las cantidades)
    porcentaje = (cantidad * 100) / totalUnidades

Cálculo de posición:
    posicion = índice del elemento + 1
    (se incrementa en el loop)
```

## 📈 Diagrama de Rendimiento

```
Request Time Breakdown (Ejemplo con 100k ventas)

Total: ~800ms
│
├─→ Controller (parsing, validation): ~10ms
│
├─→ Service (orchestration): ~5ms
│
├─→ Database Queries: ~750ms
│   ├─→ Query 1 (Ranking): ~120ms
│   ├─→ Query 2 (Descuentos): ~80ms
│   ├─→ Query 3 (Categorías): ~130ms
│   ├─→ Query 4 (Modelos): ~130ms
│   ├─→ Query 5 (Colores): ~130ms
│   ├─→ Query 6 (Tallas): ~110ms
│   └─→ Query 7 (Usuarios): ~50ms
│
├─→ Adapter (mapping, calculations): ~30ms
│
└─→ Response serialization: ~5ms

Con índices optimizados:
    Tiempo de queries: ~750ms → ~250ms
    Total: ~800ms → ~300ms
```

## 🔐 Diagrama de Seguridad

```
Request
    │
    ├─→ Header: X-Usuario-Id: 123
    │
    ▼
Controller
    │
    ├─→ Extrae idUsuario del header
    │
    ▼
Service (TODO)
    │
    ├─→ Busca usuario en DB
    │   └─→ Usuario { id: 123, rol: "VENDEDOR", idSucursal: 2 }
    │
    ├─→ if (usuario.rol != "ADMIN")
    │   └─→ filter.setIdSucursal(usuario.getIdSucursal())
    │       └─→ Ahora el filtro tiene: { ..., idSucursal: 2 }
    │
    ▼
Adapter
    │
    └─→ Todas las queries incluyen: AND v.id_sucursal = 2
        └─→ Solo retorna datos de la sucursal del vendedor
```

## 🗂️ Diagrama de Estructura de Datos

```
DashboardSalesRepResponseDTO
│
├─→ rankingVendedores: List<RankingVendedorDTO>
│   └─→ [
│       { idUsuario: 5, nombreCompleto: "Carlos", totalVendido: 45890, ... },
│       { idUsuario: 8, nombreCompleto: "María", totalVendido: 38750, ... },
│       ...
│   ]
│
├─→ analisisDescuentos: AnalisisDescuentosDTO
│   └─→ {
│       totalDescontado: 7870.0,
│       cantidadDescuentos: 80,
│       promedioPorDescuento: 98.38,
│       porcentajeSobreVentasBrutas: 6.5
│   }
│
├─→ topCategorias: List<TopItemDTO>
│   └─→ [
│       { id: 1, nombre: "Poleras", cantidad: 219, porcentaje: 35.2, posicion: 1 },
│       { id: 2, nombre: "Pantalones", cantidad: 163, porcentaje: 26.2, posicion: 2 },
│       ...
│   ]
│
├─→ topModelos: List<TopItemDTO>
│   └─→ [...]
│
├─→ topColores: List<TopItemDTO>
│   └─→ [
│       { id: 3, nombre: "Negro", cantidad: 174, porcentaje: 31.0, 
│         posicion: 1, codigoHex: "#000000" },
│       ...
│   ]
│
└─→ distribucionTallas: List<TopItemDTO>
    └─→ [...]
```

## 🔄 Diagrama de Casos de Uso

```
                    Casos de Uso del Dashboard

┌────────────────────────────────────────────────────────────────┐
│                                                                 │
│  UC1: Ver Dashboard General                                    │
│  Actor: Vendedor/Admin                                         │
│  Precondición: Usuario autenticado                             │
│  Flujo:                                                         │
│    1. Usuario abre dashboard                                   │
│    2. Sistema carga datos sin filtros                          │
│    3. Sistema muestra ranking completo                         │
│    4. Sistema muestra métricas globales                        │
│                                                                 │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│  UC2: Filtrar por Vendedor                                     │
│  Actor: Admin                                                  │
│  Precondición: Dashboard abierto                               │
│  Flujo:                                                         │
│    1. Usuario hace click en un vendedor del ranking            │
│    2. Sistema recarga dashboard con salesRepId=X               │
│    3. Sistema muestra solo datos de ese vendedor               │
│                                                                 │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│  UC3: Filtrar por Rango de Fechas                              │
│  Actor: Vendedor/Admin                                         │
│  Precondición: Dashboard abierto                               │
│  Flujo:                                                         │
│    1. Usuario selecciona rango de fechas                       │
│    2. Sistema recarga dashboard con fechas                     │
│    3. Sistema muestra datos del período seleccionado           │
│                                                                 │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│  UC4: Ver Dashboard de Mi Sucursal (Vendedor No Admin)         │
│  Actor: Vendedor                                               │
│  Precondición: Usuario NO es admin                             │
│  Flujo:                                                         │
│    1. Usuario abre dashboard                                   │
│    2. Sistema detecta que no es admin                          │
│    3. Sistema aplica filtro automático de sucursal             │
│    4. Sistema muestra solo datos de su sucursal                │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

## 🎯 Mapa Mental de Decisiones de Diseño

```
                    Dashboard Sales Rep
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
    Arquitectura        Rendimiento        Seguridad
        │                   │                   │
        ▼                   ▼                   ▼
    Hexagonal        Queries Optimizadas    Filtro Sucursal
    Clean Code       Agregaciones DB        Validación Usuario
    SOLID            Sin N+1                 Autenticación
    Ports & Adapters Índices                Headers
        │                   │                   │
        ▼                   ▼                   ▼
    Mantenible       Escalable              Seguro
    Testeable        Rápido (<1s)           Controlado
    Extensible       Eficiente              Auditado
```

---

**Nota:** Estos diagramas son representaciones conceptuales en formato texto.
Para presentaciones, considera convertirlos a diagramas visuales usando:
- PlantUML
- Mermaid
- Draw.io
- Lucidchart

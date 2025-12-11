# Ejemplos de Queries SQL - Dashboard Sales Rep

Este archivo muestra ejemplos concretos de las queries SQL que genera el sistema.

## 📊 Estructura de Tablas Relevantes

```sql
-- Tabla principal de ventas
venta (
    id_venta INTEGER PRIMARY KEY,
    fecha_venta TIMESTAMP,
    id_sucursal INTEGER,
    total_venta DOUBLE,
    descuento DOUBLE,
    tipo_descuento VARCHAR,
    estado_venta BOOLEAN,
    created_by INTEGER,  -- ID del vendedor
    ...
)

-- Detalle de cada venta
detalle_venta (
    id_detalle_venta BIGINT PRIMARY KEY,
    id_venta INTEGER,
    id_variante INTEGER,
    cantidad INTEGER,
    precio_unitario DOUBLE,
    total DOUBLE
)

-- Usuario/Vendedor
usuario (
    id_usuario INTEGER PRIMARY KEY,
    username VARCHAR,
    nombre_completo VARCHAR,
    rol VARCHAR,
    id_sucursal INTEGER
)

-- Jerarquía de productos
variante (id_variante, id_modelo_color, id_talla)
  └─> modelo_color (id_modelo_color, id_modelo, id_color)
        ├─> modelo (id_modelo, nombre_modelo, id_categoria, ...)
        │     └─> categoria (id_categoria, nombre_categoria)
        └─> color (id_color, nombre_color, codigo_hex_color)
  └─> talla (id_talla, nombre_talla)
```

## 🔍 Query 1: Ranking de Vendedores

### Sin Filtros

```sql
-- Obtiene ranking de TODOS los vendedores de TODAS las fechas
SELECT 
    v.created_by as id_vendedor,
    SUM(v.total_venta) as total_vendido,
    COUNT(v.id_venta) as cantidad_ventas
FROM venta v
WHERE v.estado_venta = true
GROUP BY v.created_by
ORDER BY SUM(v.total_venta) DESC;

-- Ejemplo de resultado:
-- id_vendedor | total_vendido | cantidad_ventas
-- ------------|---------------|----------------
-- 5           | 45890.00      | 187
-- 8           | 38750.00      | 162
-- 12          | 28450.00      | 124
```

### Con Filtro de Fechas

```sql
-- Ranking de vendedores en enero 2025
SELECT 
    v.created_by,
    SUM(v.total_venta),
    COUNT(v.id_venta)
FROM venta v
WHERE v.estado_venta = true
  AND v.fecha_venta >= '2025-01-01 00:00:00'
  AND v.fecha_venta <= '2025-01-31 23:59:59'
GROUP BY v.created_by
ORDER BY SUM(v.total_venta) DESC;
```

### Con Filtro de Vendedor

```sql
-- Solo el vendedor con id=5
SELECT 
    v.created_by,
    SUM(v.total_venta),
    COUNT(v.id_venta)
FROM venta v
WHERE v.estado_venta = true
  AND v.created_by = 5
GROUP BY v.created_by
ORDER BY SUM(v.total_venta) DESC;

-- Ejemplo de resultado:
-- created_by | total_vendido | cantidad_ventas
-- -----------|---------------|----------------
-- 5          | 45890.00      | 187
```

### Con Filtro de Sucursal (Usuario No Admin)

```sql
-- Solo ventas de la sucursal 1
SELECT 
    v.created_by,
    SUM(v.total_venta),
    COUNT(v.id_venta)
FROM venta v
WHERE v.estado_venta = true
  AND v.id_sucursal = 1
GROUP BY v.created_by
ORDER BY SUM(v.total_venta) DESC;
```

## 💰 Query 2: Análisis de Descuentos

### Consulta Completa

```sql
SELECT 
    COALESCE(SUM(v.descuento), 0) as total_descontado,
    COUNT(CASE WHEN v.descuento > 0 THEN 1 END) as cantidad_descuentos,
    COALESCE(SUM(v.total_venta + v.descuento), 0) as total_ventas_brutas
FROM venta v
WHERE v.estado_venta = true
  AND (v.fecha_venta >= '2025-01-01 00:00:00' OR NULL IS NULL)
  AND (v.fecha_venta <= '2025-12-31 23:59:59' OR NULL IS NULL)
  AND (v.created_by = 5 OR NULL IS NULL)
  AND (v.id_sucursal = 1 OR NULL IS NULL);

-- Ejemplo de resultado:
-- total_descontado | cantidad_descuentos | total_ventas_brutas
-- -----------------|---------------------|--------------------
-- 7870.00          | 80                  | 121000.00

-- Cálculos posteriores en Java:
-- promedio_por_descuento = 7870.00 / 80 = 98.38
-- porcentaje_sobre_ventas_brutas = (7870.00 / 121000.00) * 100 = 6.5%
```

### Desglose de la Lógica

```sql
-- Total descontado: suma de todos los descuentos
SELECT SUM(v.descuento) FROM venta v WHERE v.estado_venta = true;
-- Resultado: 7870.00

-- Cantidad de descuentos: cuenta solo ventas con descuento > 0
SELECT COUNT(*) FROM venta v 
WHERE v.estado_venta = true AND v.descuento > 0;
-- Resultado: 80

-- Total ventas brutas: suma de (total_venta + descuento)
-- Es decir, el precio antes del descuento
SELECT SUM(v.total_venta + v.descuento) FROM venta v 
WHERE v.estado_venta = true;
-- Resultado: 121000.00
```

## 📦 Query 3: Top Categorías

### Consulta Completa con JOINs

```sql
SELECT 
    cat.id_categoria,
    cat.nombre_categoria,
    SUM(dv.cantidad) as unidades_vendidas
FROM venta v
    INNER JOIN detalle_venta dv ON dv.id_venta = v.id_venta
    INNER JOIN variante var ON var.id_variante = dv.id_variante
    INNER JOIN modelo_color mc ON mc.id_modelo_color = var.id_modelo_color
    INNER JOIN modelo m ON m.id_modelo = mc.id_modelo
    INNER JOIN categoria cat ON cat.id_categoria = m.id_categoria
WHERE v.estado_venta = true
  AND (v.fecha_venta >= '2025-01-01 00:00:00' OR NULL IS NULL)
  AND (v.fecha_venta <= '2025-12-31 23:59:59' OR NULL IS NULL)
  AND (v.created_by = 5 OR NULL IS NULL)
  AND (v.id_sucursal = 1 OR NULL IS NULL)
GROUP BY cat.id_categoria, cat.nombre_categoria
ORDER BY SUM(dv.cantidad) DESC
LIMIT 5;

-- Ejemplo de resultado:
-- id_categoria | nombre_categoria | unidades_vendidas
-- -------------|------------------|------------------
-- 1            | Poleras          | 219
-- 2            | Pantalones       | 163
-- 3            | Hoodies          | 111
-- 4            | Shorts           | 68
-- 5            | Camisas          | 36
```

### Explicación del JOIN

```sql
-- Paso a paso:
-- 1. Empezamos con las ventas activas
SELECT * FROM venta v WHERE v.estado_venta = true;

-- 2. Traemos los detalles de cada venta
SELECT * FROM venta v
JOIN detalle_venta dv ON dv.id_venta = v.id_venta
WHERE v.estado_venta = true;

-- 3. Traemos la variante (talla + modelo_color)
SELECT * FROM venta v
JOIN detalle_venta dv ON dv.id_venta = v.id_venta
JOIN variante var ON var.id_variante = dv.id_variante
WHERE v.estado_venta = true;

-- 4. Traemos el modelo y color
SELECT * FROM venta v
JOIN detalle_venta dv ON dv.id_venta = v.id_venta
JOIN variante var ON var.id_variante = dv.id_variante
JOIN modelo_color mc ON mc.id_modelo_color = var.id_modelo_color
WHERE v.estado_venta = true;

-- 5. Traemos el modelo completo
SELECT * FROM venta v
JOIN detalle_venta dv ON dv.id_venta = v.id_venta
JOIN variante var ON var.id_variante = dv.id_variante
JOIN modelo_color mc ON mc.id_modelo_color = var.id_modelo_color
JOIN modelo m ON m.id_modelo = mc.id_modelo
WHERE v.estado_venta = true;

-- 6. Finalmente, traemos la categoría
SELECT cat.nombre_categoria, SUM(dv.cantidad)
FROM venta v
JOIN detalle_venta dv ON dv.id_venta = v.id_venta
JOIN variante var ON var.id_variante = dv.id_variante
JOIN modelo_color mc ON mc.id_modelo_color = var.id_modelo_color
JOIN modelo m ON m.id_modelo = mc.id_modelo
JOIN categoria cat ON cat.id_categoria = m.id_categoria
WHERE v.estado_venta = true
GROUP BY cat.nombre_categoria;
```

## 👕 Query 4: Top Modelos

```sql
SELECT 
    m.id_modelo,
    m.nombre_modelo,
    SUM(dv.cantidad) as unidades_vendidas
FROM venta v
    INNER JOIN detalle_venta dv ON dv.id_venta = v.id_venta
    INNER JOIN variante var ON var.id_variante = dv.id_variante
    INNER JOIN modelo_color mc ON mc.id_modelo_color = var.id_modelo_color
    INNER JOIN modelo m ON m.id_modelo = mc.id_modelo
WHERE v.estado_venta = true
  AND (v.fecha_venta >= '2025-01-01 00:00:00' OR NULL IS NULL)
  AND (v.fecha_venta <= '2025-12-31 23:59:59' OR NULL IS NULL)
  AND (v.created_by = 5 OR NULL IS NULL)
  AND (v.id_sucursal = 1 OR NULL IS NULL)
GROUP BY m.id_modelo, m.nombre_modelo
ORDER BY SUM(dv.cantidad) DESC
LIMIT 5;

-- Ejemplo de resultado:
-- id_modelo | nombre_modelo        | unidades_vendidas
-- ----------|---------------------|------------------
-- 10        | Básica Cotton       | 113
-- 15        | Slim Fit Jean       | 108
-- 22        | Classic Hoodie      | 71
-- 8         | Oversize Urban      | 68
-- 30        | Cargo Pro           | 37
```

## 🎨 Query 5: Top Colores

```sql
SELECT 
    c.id_color,
    c.nombre_color,
    c.codigo_hex_color,
    SUM(dv.cantidad) as unidades_vendidas
FROM venta v
    INNER JOIN detalle_venta dv ON dv.id_venta = v.id_venta
    INNER JOIN variante var ON var.id_variante = dv.id_variante
    INNER JOIN modelo_color mc ON mc.id_modelo_color = var.id_modelo_color
    INNER JOIN color c ON c.id_color = mc.id_color
WHERE v.estado_venta = true
  AND (v.fecha_venta >= '2025-01-01 00:00:00' OR NULL IS NULL)
  AND (v.fecha_venta <= '2025-12-31 23:59:59' OR NULL IS NULL)
  AND (v.created_by = 5 OR NULL IS NULL)
  AND (v.id_sucursal = 1 OR NULL IS NULL)
GROUP BY c.id_color, c.nombre_color, c.codigo_hex_color
ORDER BY SUM(dv.cantidad) DESC
LIMIT 5;

-- Ejemplo de resultado:
-- id_color | nombre_color | codigo_hex_color | unidades_vendidas
-- ---------|-------------|------------------|------------------
-- 3        | Negro       | #000000          | 174
-- 7        | Blanco      | #FFFFFF          | 126
-- 12       | Azul        | #0000FF          | 106
-- 5        | Gris        | #808080          | 101
-- 18       | Beige       | #F5F5DC          | 24
```

## 📏 Query 6: Distribución de Tallas

```sql
SELECT 
    t.id_talla,
    t.nombre_talla,
    SUM(dv.cantidad) as unidades_vendidas
FROM venta v
    INNER JOIN detalle_venta dv ON dv.id_venta = v.id_venta
    INNER JOIN variante var ON var.id_variante = dv.id_variante
    INNER JOIN talla t ON t.id_talla = var.id_talla
WHERE v.estado_venta = true
  AND (v.fecha_venta >= '2025-01-01 00:00:00' OR NULL IS NULL)
  AND (v.fecha_venta <= '2025-12-31 23:59:59' OR NULL IS NULL)
  AND (v.created_by = 5 OR NULL IS NULL)
  AND (v.id_sucursal = 1 OR NULL IS NULL)
GROUP BY t.id_talla, t.nombre_talla
ORDER BY SUM(dv.cantidad) DESC
LIMIT 10;

-- Ejemplo de resultado:
-- id_talla | nombre_talla | unidades_vendidas
-- ---------|-------------|------------------
-- 3        | M           | 182
-- 4        | L           | 118
-- 2        | S           | 88
-- 5        | XL          | 152
-- 6        | XXL         | 19
-- 1        | XS          | 38
```

## 🔄 Cálculo de Porcentajes (En Java)

El porcentaje se calcula después de obtener los resultados:

```java
// Ejemplo con categorías
List<Object[]> results = repository.getTopCategorias(...);

// Calcular total de unidades
long totalUnidades = results.stream()
    .mapToLong(row -> ((Number) row[2]).longValue())
    .sum();
// totalUnidades = 219 + 163 + 111 + 68 + 36 = 597

// Para cada item, calcular porcentaje
for (Object[] row : results) {
    Long cantidad = ((Number) row[2]).longValue();
    Double porcentaje = (cantidad * 100.0) / totalUnidades;
    
    // Ejemplo:
    // Poleras: (219 * 100) / 597 = 36.68%
    // Pantalones: (163 * 100) / 597 = 27.30%
    // etc.
}
```

## 🔍 Query Auxiliar: Obtener Datos de Usuarios

```sql
-- Una vez obtenidos los IDs de vendedores del ranking, 
-- se cargan los datos de todos en una sola query
SELECT 
    u.id_usuario,
    u.username,
    u.nombre_completo,
    u.rol,
    u.id_sucursal
FROM usuario u
WHERE u.id_usuario IN (5, 8, 12, 15, 20);

-- Esto evita hacer N queries (una por cada vendedor)
```

## 📊 Ejemplo Completo: Obtener Dashboard

```sql
-- Query 1: Ranking (supongamos que retorna ids: 5, 8, 12)
SELECT v.created_by, SUM(v.total_venta), COUNT(v.id_venta)
FROM venta v WHERE v.estado_venta = true
GROUP BY v.created_by
ORDER BY SUM(v.total_venta) DESC;

-- Query 2: Usuarios en batch
SELECT id_usuario, username, nombre_completo
FROM usuario WHERE id_usuario IN (5, 8, 12);

-- Query 3: Descuentos
SELECT SUM(descuento), COUNT(CASE WHEN descuento > 0 THEN 1 END), SUM(total_venta + descuento)
FROM venta WHERE estado_venta = true;

-- Query 4: Top Categorías
SELECT cat.id_categoria, cat.nombre_categoria, SUM(dv.cantidad)
FROM venta v ... [JOINs]
GROUP BY cat.id_categoria ORDER BY SUM(dv.cantidad) DESC LIMIT 5;

-- Query 5: Top Modelos
SELECT m.id_modelo, m.nombre_modelo, SUM(dv.cantidad)
FROM venta v ... [JOINs]
GROUP BY m.id_modelo ORDER BY SUM(dv.cantidad) DESC LIMIT 5;

-- Query 6: Top Colores
SELECT c.id_color, c.nombre_color, c.codigo_hex_color, SUM(dv.cantidad)
FROM venta v ... [JOINs]
GROUP BY c.id_color ORDER BY SUM(dv.cantidad) DESC LIMIT 5;

-- Query 7: Distribución Tallas
SELECT t.id_talla, t.nombre_talla, SUM(dv.cantidad)
FROM venta v ... [JOINs]
GROUP BY t.id_talla ORDER BY SUM(dv.cantidad) DESC LIMIT 10;

-- TOTAL: 7 queries para el dashboard completo
```

## ⚡ Optimización con Índices

```sql
-- Crear índices para mejorar el rendimiento

-- Índice en estado_venta (usado en todos los WHEREs)
CREATE INDEX idx_venta_estado ON venta(estado_venta);

-- Índice en fecha_venta (para rangos de fechas)
CREATE INDEX idx_venta_fecha ON venta(fecha_venta);

-- Índice en created_by (para filtro de vendedor)
CREATE INDEX idx_venta_created_by ON venta(created_by);

-- Índice en id_sucursal (para filtro de sucursal)
CREATE INDEX idx_venta_sucursal ON venta(id_sucursal);

-- Índice compuesto (el más eficiente)
CREATE INDEX idx_venta_dashboard 
ON venta(estado_venta, fecha_venta, created_by, id_sucursal);

-- Índices en foreign keys (para los JOINs)
CREATE INDEX idx_detalle_venta_id_venta ON detalle_venta(id_venta);
CREATE INDEX idx_detalle_venta_id_variante ON detalle_venta(id_variante);
CREATE INDEX idx_variante_id_modelo_color ON variante(id_modelo_color);
CREATE INDEX idx_variante_id_talla ON variante(id_talla);
CREATE INDEX idx_modelo_color_id_modelo ON modelo_color(id_modelo);
CREATE INDEX idx_modelo_color_id_color ON modelo_color(id_color);
CREATE INDEX idx_modelo_id_categoria ON modelo(id_categoria);
```

## 🧪 Verificar el Plan de Ejecución

```sql
-- Para PostgreSQL
EXPLAIN ANALYZE
SELECT v.created_by, SUM(v.total_venta), COUNT(v.id_venta)
FROM venta v
WHERE v.estado_venta = true
  AND v.fecha_venta >= '2025-01-01'
  AND v.fecha_venta <= '2025-12-31'
GROUP BY v.created_by
ORDER BY SUM(v.total_venta) DESC;

-- Busca:
-- - "Seq Scan" vs "Index Scan" (Index Scan es mejor)
-- - Tiempo de ejecución (execution time)
-- - Rows estimadas vs reales
```

## 📈 Estadísticas de Ejemplo

```sql
-- Verificar cantidad de datos
SELECT 
    'ventas' as tabla,
    COUNT(*) as total,
    COUNT(CASE WHEN estado_venta = true THEN 1 END) as activas,
    COUNT(CASE WHEN descuento > 0 THEN 1 END) as con_descuento
FROM venta;

SELECT 
    'detalles' as tabla,
    COUNT(*) as total,
    SUM(cantidad) as unidades_totales
FROM detalle_venta;

-- Resultado ejemplo:
-- tabla    | total  | activas | con_descuento
-- ---------|--------|---------|---------------
-- ventas   | 15420  | 15200   | 1234
-- detalles | 45890  | -       | -
```

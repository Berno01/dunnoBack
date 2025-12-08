# Guía de Ejemplos de Uso - Dashboard API

## Ejemplos de Peticiones HTTP

### 1. KPIs del Dashboard

**Obtener KPIs de hoy (todas las sucursales)**

```http
GET http://localhost:8080/api/dashboard/kpis
```

**Obtener KPIs de una sucursal específica**

```http
GET http://localhost:8080/api/dashboard/kpis?idSucursal=1
```

**Obtener KPIs de un rango de fechas**

```http
GET http://localhost:8080/api/dashboard/kpis?fechaInicio=2025-12-01&fechaFin=2025-12-08
```

**Obtener KPIs de sucursal en rango de fechas**

```http
GET http://localhost:8080/api/dashboard/kpis?idSucursal=2&fechaInicio=2025-12-01&fechaFin=2025-12-08
```

---

### 2. Ventas por Hora

**Obtener ventas por hora del día actual**

```http
GET http://localhost:8080/api/dashboard/ventas-por-hora
```

**Obtener ventas por hora de una fecha específica**

```http
GET http://localhost:8080/api/dashboard/ventas-por-hora?fechaInicio=2025-12-08&fechaFin=2025-12-08
```

**Obtener ventas por hora de sucursal específica**

```http
GET http://localhost:8080/api/dashboard/ventas-por-hora?idSucursal=1
```

---

### 3. Ventas por Categoría

**Obtener ventas por categoría de hoy**

```http
GET http://localhost:8080/api/dashboard/ventas-por-categoria
```

**Obtener ventas por categoría del último mes**

```http
GET http://localhost:8080/api/dashboard/ventas-por-categoria?fechaInicio=2025-11-08&fechaFin=2025-12-08
```

**Obtener ventas por categoría de sucursal específica**

```http
GET http://localhost:8080/api/dashboard/ventas-por-categoria?idSucursal=3
```

---

### 4. Métodos de Pago

**Obtener métodos de pago usados hoy**

```http
GET http://localhost:8080/api/dashboard/metodos-pago
```

**Obtener métodos de pago de una semana**

```http
GET http://localhost:8080/api/dashboard/metodos-pago?fechaInicio=2025-12-01&fechaFin=2025-12-08
```

**Obtener métodos de pago de sucursal específica**

```http
GET http://localhost:8080/api/dashboard/metodos-pago?idSucursal=1
```

---

### 5. Distribución de Tallas

**Obtener distribución de tallas vendidas hoy**

```http
GET http://localhost:8080/api/dashboard/distribucion-tallas
```

**Obtener distribución de tallas de un mes**

```http
GET http://localhost:8080/api/dashboard/distribucion-tallas?fechaInicio=2025-11-01&fechaFin=2025-11-30
```

**Obtener distribución de tallas por sucursal**

```http
GET http://localhost:8080/api/dashboard/distribucion-tallas?idSucursal=2
```

---

### 6. Top Productos

**Obtener top 5 productos más vendidos (default)**

```http
GET http://localhost:8080/api/dashboard/top-productos
```

**Obtener top 10 productos más vendidos**

```http
GET http://localhost:8080/api/dashboard/top-productos?limit=10
```

**Obtener top 5 de una sucursal específica**

```http
GET http://localhost:8080/api/dashboard/top-productos?idSucursal=1
```

**Obtener top 10 del último mes**

```http
GET http://localhost:8080/api/dashboard/top-productos?limit=10&fechaInicio=2025-11-08&fechaFin=2025-12-08
```

**Obtener top 3 de sucursal en rango de fechas**

```http
GET http://localhost:8080/api/dashboard/top-productos?idSucursal=2&limit=3&fechaInicio=2025-12-01&fechaFin=2025-12-08
```

---

## Respuestas de Ejemplo

### KPIs Response

```json
{
  "totalVentas": 45780.5,
  "cantidadVentas": 152,
  "ticketPromedio": 301.19,
  "unidadesVendidas": 387
}
```

### Ventas por Hora Response

```json
[
  { "hora": 8, "total": 1250.0 },
  { "hora": 9, "total": 3400.5 },
  { "hora": 10, "total": 5620.0 },
  { "hora": 11, "total": 4890.25 },
  { "hora": 14, "total": 6750.0 },
  { "hora": 15, "total": 8920.75 },
  { "hora": 16, "total": 7450.0 },
  { "hora": 17, "total": 5200.0 },
  { "hora": 18, "total": 2300.0 }
]
```

### Ventas por Categoría Response

```json
[
  { "categoria": "Poleras", "cantidad": 125 },
  { "categoria": "Pantalones", "cantidad": 98 },
  { "categoria": "Casacas", "cantidad": 76 },
  { "categoria": "Shorts", "cantidad": 54 },
  { "categoria": "Buzos", "cantidad": 34 }
]
```

### Métodos de Pago Response

```json
[
  { "metodo": "Efectivo", "cantidad": 85 },
  { "metodo": "QR", "cantidad": 42 },
  { "metodo": "Tarjeta", "cantidad": 25 }
]
```

### Distribución de Tallas Response

```json
[
  { "talla": "M", "cantidad": 142 },
  { "talla": "L", "cantidad": 108 },
  { "talla": "S", "cantidad": 87 },
  { "talla": "XL", "cantidad": 35 },
  { "talla": "XS", "cantidad": 15 }
]
```

### Top Productos Response

```json
[
  {
    "nombreModelo": "Polo Básico Algodón",
    "subtitulo": "Poleras - Nike",
    "cantidadVendida": 87,
    "stockActual": 245,
    "fotoUrl": "https://storage.example.com/productos/polo-basico-nike.jpg"
  },
  {
    "nombreModelo": "Jean Slim Fit",
    "subtitulo": "Pantalones - Levi's",
    "cantidadVendida": 65,
    "stockActual": 132,
    "fotoUrl": "https://storage.example.com/productos/jean-slim-levis.jpg"
  },
  {
    "nombreModelo": "Casaca Deportiva",
    "subtitulo": "Casacas - Adidas",
    "cantidadVendida": 54,
    "stockActual": 89,
    "fotoUrl": "https://storage.example.com/productos/casaca-adidas.jpg"
  },
  {
    "nombreModelo": "Polera Manga Larga",
    "subtitulo": "Poleras - Puma",
    "cantidadVendida": 48,
    "stockActual": 156,
    "fotoUrl": "https://storage.example.com/productos/polera-puma.jpg"
  },
  {
    "nombreModelo": "Short Deportivo",
    "subtitulo": "Shorts - Nike",
    "cantidadVendida": 42,
    "stockActual": 98,
    "fotoUrl": "https://storage.example.com/productos/short-nike.jpg"
  }
]
```

---

## Casos de Uso Frontend

### Dashboard Principal

```javascript
// Obtener KPIs del día actual (todas las sucursales)
const response = await fetch("/api/dashboard/kpis");
const kpis = await response.json();

// Mostrar en cards:
// - Total Ventas: Bs. 45,780.50
// - Cantidad de Ventas: 152 transacciones
// - Ticket Promedio: Bs. 301.19
// - Unidades Vendidas: 387 productos
```

### Gráfico de Ventas por Hora

```javascript
// Obtener datos para gráfico de líneas o barras
const response = await fetch("/api/dashboard/ventas-por-hora");
const ventasPorHora = await response.json();

// Usar en Chart.js, ApexCharts, etc.
// X: Hora del día (0-23)
// Y: Total de ventas
```

### Gráfico de Torta - Categorías

```javascript
// Obtener datos para gráfico de torta
const response = await fetch("/api/dashboard/ventas-por-categoria");
const categorias = await response.json();

// Mostrar distribución de ventas por categoría
```

### Filtro por Sucursal (Selector)

```javascript
// Usuario selecciona sucursal del dropdown
const sucursalId = document.getElementById("sucursal").value;

// Actualizar todos los widgets del dashboard
const kpis = await fetch(`/api/dashboard/kpis?idSucursal=${sucursalId}`);
const ventas = await fetch(
  `/api/dashboard/ventas-por-hora?idSucursal=${sucursalId}`
);
const categorias = await fetch(
  `/api/dashboard/ventas-por-categoria?idSucursal=${sucursalId}`
);
// etc...
```

### Filtro por Rango de Fechas

```javascript
// Usuario selecciona rango de fechas
const fechaInicio = document.getElementById("fechaInicio").value; // 2025-12-01
const fechaFin = document.getElementById("fechaFin").value; // 2025-12-08

// Actualizar dashboard con rango
const url = `/api/dashboard/kpis?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;
const response = await fetch(url);
const kpis = await response.json();
```

### Widget Top Productos

```javascript
// Mostrar los 5 productos más vendidos con imágenes
const response = await fetch("/api/dashboard/top-productos?limit=5");
const topProductos = await response.json();

topProductos.forEach((producto) => {
  // Renderizar card con:
  // - Imagen: producto.fotoUrl
  // - Título: producto.nombreModelo
  // - Subtítulo: producto.subtitulo (Categoría - Marca)
  // - Badge: "Vendidos: ${producto.cantidadVendida}"
  // - Stock: "Stock actual: ${producto.stockActual}"
  // - Indicador de stock bajo si stockActual < umbral
});
```

---

## Notas de Integración

1. **Formato de Fechas**: Usar formato ISO 8601 (`YYYY-MM-DD`)
2. **Valores NULL**: Si no se envían parámetros, usa valores por defecto (hoy, todas las sucursales)
3. **Manejo de Errores**: Todos los endpoints retornan valores por defecto (0, listas vacías) si no hay datos
4. **CORS**: Asegurarse que el frontend esté configurado en `CorsConfig.java`
5. **Stock en Tiempo Real**: El stock se calcula en tiempo real desde la tabla inventario

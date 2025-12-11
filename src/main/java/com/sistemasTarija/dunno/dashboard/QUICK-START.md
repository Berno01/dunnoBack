# 🚀 Quick Start - Dashboard de Vendedores

## ⚡ Inicio Rápido en 5 Minutos

### 1️⃣ Verificar que Todo Compila

```bash
cd d:\SISTEMAS-CLIENTES\DUNNO\Implementation\BackEnd
mvn clean compile
```

**Resultado esperado:** `BUILD SUCCESS`

### 2️⃣ Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

**Resultado esperado:** Aplicación corriendo en `http://localhost:8080`

### 3️⃣ Probar el Endpoint

Abre una terminal nueva y ejecuta:

```bash
# Probar sin filtros (todos los vendedores, todas las fechas)
curl -X GET "http://localhost:8080/api/dashboard/sales-rep-analysis" \
  -H "X-Usuario-Id: 1"
```

**Resultado esperado:** JSON con todas las secciones del dashboard

### 4️⃣ Probar con Filtros

```bash
# Probar con filtro de vendedor
curl -X GET "http://localhost:8080/api/dashboard/sales-rep-analysis?salesRepId=5" \
  -H "X-Usuario-Id: 1"

# Probar con rango de fechas
curl -X GET "http://localhost:8080/api/dashboard/sales-rep-analysis?startDate=2025-01-01&endDate=2025-12-31" \
  -H "X-Usuario-Id: 1"
```

### 5️⃣ Listo!

Si ves datos JSON, ¡el backend está funcionando! 🎉

---

## 📚 Documentación Completa

Para detalles completos, consulta estos archivos en orden:

1. **RESUMEN.md** - Resumen ejecutivo (léeme primero)
2. **README-SALES-REP.md** - Documentación completa del módulo
3. **IMPLEMENTACION.md** - Guía de implementación detallada
4. **SQL-EXAMPLES.md** - Ejemplos de queries SQL
5. **DIAGRAMAS.md** - Diagramas visuales de arquitectura
6. **CHECKLIST.md** - Lista de verificación

---

## 🎯 Endpoints Disponibles

### GET /api/dashboard/sales-rep-analysis

**Descripción:** Obtiene todos los datos del dashboard

**Query Parameters:**
- `startDate` (opcional): Fecha inicio `yyyy-MM-dd`
- `endDate` (opcional): Fecha fin `yyyy-MM-dd`
- `salesRepId` (opcional): ID del vendedor

**Headers:**
- `X-Usuario-Id` (requerido): ID del usuario

**Response:**
```json
{
  "rankingVendedores": [...],
  "analisisDescuentos": {...},
  "topCategorias": [...],
  "topModelos": [...],
  "topColores": [...],
  "distribucionTallas": [...]
}
```

---

## 🔧 Configuración Recomendada

### Índices de Base de Datos (Opcional pero Recomendado)

```sql
-- Ejecuta estos comandos en tu PostgreSQL para mejorar el rendimiento

CREATE INDEX idx_venta_dashboard 
ON venta(estado_venta, fecha_venta, created_by, id_sucursal);

CREATE INDEX idx_detalle_venta_id_venta ON detalle_venta(id_venta);
CREATE INDEX idx_detalle_venta_id_variante ON detalle_venta(id_variante);
CREATE INDEX idx_variante_id_modelo_color ON variante(id_modelo_color);
CREATE INDEX idx_variante_id_talla ON variante(id_talla);
CREATE INDEX idx_modelo_color_id_modelo ON modelo_color(id_modelo);
CREATE INDEX idx_modelo_color_id_color ON modelo_color(id_color);
CREATE INDEX idx_modelo_id_categoria ON modelo(id_categoria);
```

---

## 📝 TODO Pendiente Crítico

En el archivo `DashboardSalesRepController.java`, línea 56-60, hay un TODO:

```java
// TODO: Aquí deberías verificar si el usuario es admin o no
// Si no es admin, agregar el filtro de sucursal:
Usuario usuario = usuarioService.findById(idUsuario);
if (!usuario.isAdmin()) {
    filter.setIdSucursal(usuario.getIdSucursal());
}
```

**Acción requerida:** Implementar esta lógica para que los vendedores no admin solo vean datos de su sucursal.

---

## 🧪 Tests Rápidos con Postman/Insomnia

### Request 1: Dashboard General
```
GET http://localhost:8080/api/dashboard/sales-rep-analysis
Headers:
  X-Usuario-Id: 1
```

### Request 2: Filtrado por Vendedor
```
GET http://localhost:8080/api/dashboard/sales-rep-analysis?salesRepId=5
Headers:
  X-Usuario-Id: 1
```

### Request 3: Filtrado por Fechas
```
GET http://localhost:8080/api/dashboard/sales-rep-analysis?startDate=2025-01-01&endDate=2025-12-31
Headers:
  X-Usuario-Id: 1
```

### Request 4: Todos los Filtros
```
GET http://localhost:8080/api/dashboard/sales-rep-analysis?startDate=2025-01-01&endDate=2025-01-31&salesRepId=5
Headers:
  X-Usuario-Id: 1
```

---

## 🎨 Ejemplo de Integración Frontend (React)

```typescript
import { useState, useEffect } from 'react';

interface DashboardData {
  rankingVendedores: any[];
  analisisDescuentos: any;
  topCategorias: any[];
  topModelos: any[];
  topColores: any[];
  distribucionTallas: any[];
}

export const useDashboard = () => {
  const [data, setData] = useState<DashboardData | null>(null);
  const [filters, setFilters] = useState({
    startDate: null,
    endDate: null,
    salesRepId: null
  });

  useEffect(() => {
    const fetchDashboard = async () => {
      const params = new URLSearchParams();
      if (filters.startDate) params.append('startDate', filters.startDate);
      if (filters.endDate) params.append('endDate', filters.endDate);
      if (filters.salesRepId) params.append('salesRepId', filters.salesRepId);

      const response = await fetch(
        `http://localhost:8080/api/dashboard/sales-rep-analysis?${params}`,
        {
          headers: {
            'X-Usuario-Id': '1' // Obtener del contexto de autenticación
          }
        }
      );

      const data = await response.json();
      setData(data);
    };

    fetchDashboard();
  }, [filters]);

  return { data, filters, setFilters };
};

// Uso en componente
const Dashboard = () => {
  const { data, filters, setFilters } = useDashboard();

  if (!data) return <div>Cargando...</div>;

  return (
    <div>
      <h1>Dashboard de Vendedores</h1>
      
      {/* Ranking */}
      <section>
        <h2>Ranking de Vendedores</h2>
        {data.rankingVendedores.map(vendedor => (
          <div 
            key={vendedor.idUsuario}
            onClick={() => setFilters({...filters, salesRepId: vendedor.idUsuario})}
          >
            <span>{vendedor.posicion}. {vendedor.nombreCompleto}</span>
            <span>Bs. {vendedor.totalVendido}</span>
            <span>{vendedor.cantidadVentas} ventas</span>
          </div>
        ))}
      </section>

      {/* Descuentos */}
      <section>
        <h2>Análisis de Descuentos</h2>
        <div>Total: Bs. {data.analisisDescuentos.totalDescontado}</div>
        <div>Cantidad: {data.analisisDescuentos.cantidadDescuentos}</div>
        <div>Promedio: Bs. {data.analisisDescuentos.promedioPorDescuento}</div>
        <div>% Ventas Brutas: {data.analisisDescuentos.porcentajeSobreVentasBrutas}%</div>
      </section>

      {/* Tops */}
      <section>
        <h2>Top Categorías</h2>
        {data.topCategorias.map(cat => (
          <div key={cat.id}>
            {cat.nombre}: {cat.cantidad} u. ({cat.porcentaje}%)
          </div>
        ))}
      </section>
    </div>
  );
};
```

---

## 🐛 Troubleshooting Rápido

### Error: "Cannot resolve symbol DashboardSalesRepResponseDTO"

**Solución:** Ejecuta `mvn clean compile` para regenerar las clases

### Error: "No handler found for /api/dashboard/sales-rep-analysis"

**Solución:** Verifica que la aplicación está corriendo y que el puerto es el correcto

### Error: "Connection refused"

**Solución:** Verifica que PostgreSQL está corriendo y que las credenciales en `application.yaml` son correctas

### Sin datos en la respuesta

**Solución:** Verifica que hay ventas con `estado_venta = true` en la base de datos

```sql
SELECT COUNT(*) FROM venta WHERE estado_venta = true;
```

---

## 📞 Ayuda

Si tienes problemas:

1. ✅ Revisa que la aplicación compiló correctamente
2. ✅ Revisa los logs en la consola
3. ✅ Verifica que hay datos en la base de datos
4. ✅ Consulta la documentación completa en `README-SALES-REP.md`

---

## ✨ Próximos Pasos

1. ✅ Backend funcionando (¡Ya está!)
2. ⏳ Implementar filtro de sucursal (TODO en controller)
3. ⏳ Crear tests unitarios
4. ⏳ Crear tests de integración
5. ⏳ Crear índices en base de datos
6. ⏳ Integrar con frontend
7. ⏳ Deploy a producción

---

**¡Felicitaciones! El backend del Dashboard de Vendedores está listo para usar.** 🎉

Para más detalles, consulta la documentación completa en los otros archivos .md

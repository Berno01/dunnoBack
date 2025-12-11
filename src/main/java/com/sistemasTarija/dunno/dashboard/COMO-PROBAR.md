# 🧪 Cómo Probar el Endpoint Correctamente

## ⚠️ Problema Común

El endpoint **GET** no acepta JSON en el body. Los parámetros deben ir en la URL.

## ✅ Forma Correcta de Probar

### Opción 1: GET con Parámetros en la URL (RECOMENDADO)

#### Sin Filtros (Todos los vendedores, todas las fechas)
```
GET http://localhost:8080/api/dashboard/sales-rep-analysis
Headers:
  X-Usuario-Id: 1
```

#### Con Filtro de Fechas
```
GET http://localhost:8080/api/dashboard/sales-rep-analysis?startDate=2025-12-11&endDate=2025-12-11
Headers:
  X-Usuario-Id: 1
```

#### Con Filtro de Vendedor
```
GET http://localhost:8080/api/dashboard/sales-rep-analysis?salesRepId=1
Headers:
  X-Usuario-Id: 1
```

#### Con Todos los Filtros
```
GET http://localhost:8080/api/dashboard/sales-rep-analysis?startDate=2025-12-11&endDate=2025-12-11&salesRepId=1
Headers:
  X-Usuario-Id: 1
```

### Opción 2: POST con JSON en el Body

Si prefieres enviar los filtros en el body como JSON:

```
POST http://localhost:8080/api/dashboard/sales-rep-analysis
Headers:
  X-Usuario-Id: 1
  Content-Type: application/json

Body (JSON):
{
    "startDate": "2025-12-11",
    "endDate": "2025-12-11",
    "salesRepId": 1
}
```

## 📋 Configuración en Postman

### Para GET:
1. Método: **GET**
2. URL: `http://localhost:8080/api/dashboard/sales-rep-analysis`
3. **Params** (pestaña):
   - Key: `startDate` | Value: `2025-12-11`
   - Key: `endDate` | Value: `2025-12-11`
   - Key: `salesRepId` | Value: `1`
4. **Headers** (pestaña):
   - Key: `X-Usuario-Id` | Value: `1`

### Para POST:
1. Método: **POST**
2. URL: `http://localhost:8080/api/dashboard/sales-rep-analysis`
3. **Headers** (pestaña):
   - Key: `X-Usuario-Id` | Value: `1`
   - Key: `Content-Type` | Value: `application/json`
4. **Body** (pestaña):
   - Seleccionar: **raw**
   - Formato: **JSON**
   - Contenido:
     ```json
     {
         "startDate": "2025-12-11",
         "endDate": "2025-12-11",
         "salesRepId": 1
     }
     ```

## 🔍 Verificar que el Backend Recibió los Filtros

Revisa los logs. Deberías ver:

```
INFO - GET /api/dashboard/sales-rep-analysis - Usuario: 1, Fechas: 2025-12-11 a 2025-12-11, SalesRep: 1
INFO - Obteniendo datos del dashboard con filtros: startDate=2025-12-11, endDate=2025-12-11, salesRepId=1, idSucursal=null
```

Si ves `null` en los filtros, significa que no están llegando correctamente.

## 🐛 Error que Estabas Teniendo

**Problema:** Intentaste hacer GET con JSON en el body
```
GET http://localhost:8080/api/dashboard/sales-rep-analysis
Body: { "startDate": "2025-12-11", ... }  ❌ INCORRECTO
```

**Solución:** Usa parámetros en la URL o usa POST
```
GET http://localhost:8080/api/dashboard/sales-rep-analysis?startDate=2025-12-11  ✅ CORRECTO
```

## 💡 Notas Importantes

1. **Header correcto**: `X-Usuario-Id` (no `X-User-Id`)
2. **Formato de fechas**: `yyyy-MM-dd` (ejemplo: `2025-12-11`)
3. **Los filtros son opcionales**: Puedes omitir cualquiera de ellos
4. **GET vs POST**: Usa GET para consultas simples, POST si necesitas enviar muchos filtros

## 🎯 Casos de Prueba Recomendados

### Test 1: Dashboard Completo
```bash
curl -X GET "http://localhost:8080/api/dashboard/sales-rep-analysis" \
  -H "X-Usuario-Id: 1"
```

### Test 2: Filtrado por Fecha (Hoy)
```bash
curl -X GET "http://localhost:8080/api/dashboard/sales-rep-analysis?startDate=2025-12-11&endDate=2025-12-11" \
  -H "X-Usuario-Id: 1"
```

### Test 3: Filtrado por Vendedor
```bash
curl -X GET "http://localhost:8080/api/dashboard/sales-rep-analysis?salesRepId=1" \
  -H "X-Usuario-Id: 1"
```

### Test 4: Todos los Filtros
```bash
curl -X GET "http://localhost:8080/api/dashboard/sales-rep-analysis?startDate=2025-12-01&endDate=2025-12-31&salesRepId=1" \
  -H "X-Usuario-Id: 1"
```

## ✅ Respuesta Esperada

Deberías recibir un JSON como este:

```json
{
    "rankingVendedores": [
        {
            "idUsuario": 1,
            "nombreCompleto": "Nombre del Usuario",
            "username": "username",
            "totalVendido": 1500.50,
            "cantidadVentas": 25,
            "posicion": 1
        }
    ],
    "analisisDescuentos": {
        "totalDescontado": 250.00,
        "cantidadDescuentos": 10,
        "promedioPorDescuento": 25.00,
        "porcentajeSobreVentasBrutas": 14.3
    },
    "topCategorias": [...],
    "topModelos": [...],
    "topColores": [...],
    "distribucionTallas": [...]
}
```

## 🔧 Si Aún No Funciona

1. **Verifica que la aplicación está corriendo**: `mvn spring-boot:run`
2. **Revisa los logs**: Busca errores en la consola
3. **Verifica que hay datos**: Ejecuta en tu base de datos:
   ```sql
   SELECT COUNT(*) FROM venta WHERE estado_venta = true;
   ```
4. **Verifica el puerto**: Asegúrate de usar el puerto correcto (por defecto 8080)

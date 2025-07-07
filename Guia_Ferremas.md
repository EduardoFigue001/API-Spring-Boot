# 📋 GUÍA DE USO COMPLETA - FERREMAS API
## Manual de Usuario y Administración

---

## 🚀 **INICIO RÁPIDO**

### 1. **Ejecutar la Aplicación**
```bash
# Navegar al directorio del proyecto
cd C:\Users\andre\Documents\GitHub\API-Spring-Boot

# Ejecutar la aplicación
mvn spring-boot:run
```

### 2. **Verificar que funciona**
- La aplicación se ejecuta en: **http://localhost:8080**
- Swagger UI disponible en: **http://localhost:8080/swagger-ui.html**

---

## 👤 **USUARIOS ADMINISTRADORES**

### **Usuario Admin Principal**
- **URL Login**: http://localhost:8080/admin.html
- **Correo**: `admin@ferremas.com`
- **Contraseña**: `admin123`
- **Permisos**: Crear/editar/eliminar productos, crear otros admins

### **Clave Maestra para Crear Nuevos Admins**
- **Clave**: `ferremas2024`

---

## 🌐 **URLS PRINCIPALES**

### **Frontend - Páginas Web**
| Página | URL | Descripción |
|--------|-----|-------------|
| **Inicio/Login** | http://localhost:8080/index.html | Página principal con login |
| **Registro** | http://localhost:8080/registro.html | Registro de nuevos usuarios |
| **Catálogo** | http://localhost:8080/catalogo.html | Ver productos disponibles |
| **Carrito** | http://localhost:8080/carro.html | Gestión del carrito de compras |
| **Home** | http://localhost:8080/home.html | Dashboard después del login |
| **Admin Panel** | http://localhost:8080/admin.html | Panel de administración |
| **Test APIs** | http://localhost:8080/test-registro.html | Página de pruebas |
| **Conversor** | http://localhost:8080/conversor.html | Conversión entre múltiples monedas |
| **Historial de Compras** | http://localhost:8080/historial.html | Ver historial completo de compras del usuario |

### **API Endpoints**
| Método | URL | Descripción |
|--------|-----|-------------|
| **GET** | http://localhost:8080/api/productos | Listar todos los productos |
| **GET** | http://localhost:8080/api/productos/{codigo} | Obtener producto específico |
| **POST** | http://localhost:8080/api/productos/admin | Crear producto (solo admins) |
| **POST** | http://localhost:8080/api/usuarios/registrar | Registrar usuario |
| **POST** | http://localhost:8080/api/usuarios/login | Login de usuario |
| **POST** | http://localhost:8080/api/usuarios/crear-admin | Crear administrador |
| **GET** | http://localhost:8080/api/divisa/dolar | Valor del dólar |
| **GET** | http://localhost:8080/swagger-ui.html | Documentación API |
| **GET** | http://localhost:8080/api/divisa/monedas | Obtener todas las monedas y sus valores |
| **GET** | http://localhost:8080/api/divisa/moneda/{codigo} | Obtener valor de moneda específica |
| **POST** | http://localhost:8080/api/divisa/convertir-universal | Convertir entre cualquier par de monedas |
| **POST** | http://localhost:8080/api/divisa/convertir-multiples | Conversiones múltiples de una vez |
| **GET** | http://localhost:8080/api/divisa/monedas-soportadas | Lista de monedas soportadas |
| **POST** | http://localhost:8080/api/compras | Crear una nueva compra |
| **GET** | http://localhost:8080/api/compras/usuario/{usuarioId} | Obtener historial de compras de un usuario |
| **GET** | http://localhost:8080/api/compras/{compraId} | Obtener detalles de una compra específica |
| **PUT** | http://localhost:8080/api/compras/{compraId}/cancelar | Cancelar una compra (si está pendiente) |

---

## 📖 **GUÍA DE USO PASO A PASO**

### **PARA USUARIOS NORMALES**

#### **1. Registrarse en el Sistema**
1. Ir a: http://localhost:8080/registro.html
2. Llenar el formulario:
   - **Correo**: usuario@ejemplo.com
   - **Contraseña**: mínimo 6 caracteres
3. Hacer clic en "Registrar"
4. Recibir confirmación de registro exitoso

#### **2. Iniciar Sesión**
1. Ir a: http://localhost:8080/index.html
2. Ingresar credenciales:
   - **Correo**: el registrado anteriormente
   - **Contraseña**: la elegida
3. Hacer clic en "Iniciar Sesión"
4. Ser redirigido al dashboard

#### **3. Ver Catálogo de Productos**
1. Ir a: http://localhost:8080/catalogo.html
2. Explorar productos disponibles
3. Usar filtros por nombre si es necesario
4. Ver precios, marcas y categorías

#### **4. Gestionar Carrito**
1. En el catálogo, hacer clic en "Agregar al Carro"
2. Ir a: http://localhost:8080/carro.html
3. Ver productos agregados
4. Modificar cantidades o eliminar productos
5. Proceder al pago cuando esté listo

#### **5. Realizar una Compra**
1. Ir a: http://localhost:8080/carro.html
2. Revisar los productos en el carrito
3. Hacer clic en "Finalizar Compra"
4. La compra se registrará automáticamente

#### **6. Ver Historial de Compras**
1. Ir a: http://localhost:8080/historial.html
2. Ver el historial completo de compras
3. Filtrar por estado de compra si es necesario

---

### **PARA ADMINISTRADORES**

#### **1. Acceder al Panel de Administración**
1. Ir a: http://localhost:8080/admin.html
2. Usar credenciales de admin:
   - **Correo**: `admin@ferremas.com`
   - **Contraseña**: `admin123`
3. Hacer clic en "Iniciar Sesión"

#### **2. Gestionar Productos Existentes**
1. En el panel admin, ir a la pestaña "Gestionar Productos"
2. Ver lista completa de productos
3. Para eliminar: hacer clic en "Eliminar" y confirmar
4. Para recargar lista: hacer clic en "Recargar Lista"

#### **3. Crear Nuevos Productos**
1. Ir a la pestaña "Crear Producto"
2. Llenar el formulario:
   - **Código del Producto**: Único (ej: FER-12345)
   - **Nombre**: Nombre descriptivo
   - **Marca**: Marca del producto
   - **Precio**: Precio en pesos chilenos
   - **Stock**: Cantidad disponible
   - **Categoría**: Seleccionar de la lista
   - **Descripción**: Descripción detallada
3. Hacer clic en "Crear Producto"
4. Recibir confirmación de creación exitosa

#### **4. Crear Nuevos Administradores**
1. Ir a la pestaña "Crear Admin"
2. Llenar el formulario:
   - **Correo del nuevo admin**: email válido
   - **Contraseña**: contraseña segura
   - **Clave maestra**: `ferremas2024`
3. Hacer clic en "Crear Administrador"
4. El nuevo admin podrá acceder al panel

---

## 📦 **SISTEMA DE COMPRAS Y HISTORIAL**

### **Páginas del Sistema de Compras**
| Página | URL | Descripción |
|--------|-----|-------------|
| **Historial de Compras** | http://localhost:8080/historial.html | Ver historial completo de compras del usuario |

### **API Endpoints de Compras**
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| **POST** | `/api/compras` | Crear una nueva compra |
| **GET** | `/api/compras/usuario/{usuarioId}` | Obtener historial de compras de un usuario |
| **GET** | `/api/compras/{compraId}` | Obtener detalles de una compra específica |
| **PUT** | `/api/compras/{compraId}/cancelar` | Cancelar una compra (si está pendiente) |

### **Flujo de Compra Completo**

#### 1. **Agregar Productos al Carrito**
- Navegar a http://localhost:8080/catalogo.html
- Seleccionar productos y agregarlos al carrito
- Los productos se almacenan en localStorage del navegador

#### 2. **Finalizar Compra**
- Ir a http://localhost:8080/carro.html
- Revisar productos y total
- Hacer clic en "Finalizar Compra"
- **Importante**: Debes estar logueado para finalizar

#### 3. **Registro Automático**
- La compra se registra automáticamente en la base de datos
- Se actualizan los stocks de productos
- Se genera un archivo de resumen descargable
- Se asigna un ID único a la compra

#### 4. **Ver Historial**
- Acceder desde http://localhost:8080/home.html → "📋 Historial de Compras"
- O directamente: http://localhost:8080/historial.html
- Ver todas las compras con estados y detalles
- Filtrar por estado de compra

### **Estados de Compra**
| Estado | Descripción |
|--------|-------------|
| **PENDIENTE** | Compra recién creada, puede cancelarse |
| **CONFIRMADA** | Compra confirmada por el sistema |
| **PROCESANDO** | Preparando el pedido |
| **ENVIADA** | Pedido en camino |
| **ENTREGADA** | Pedido completado |
| **CANCELADA** | Compra cancelada, stock restaurado |

### **Características del Sistema**
- ✅ **Control de Stock**: Se descuenta automáticamente al comprar
- ✅ **Restauración de Stock**: Se restaura al cancelar compras
- ✅ **Historial Completo**: Todas las compras quedan registradas
- ✅ **Detalles de Productos**: Nombres, precios y cantidades guardadas
- ✅ **Filtros Avanzados**: Por estado, fecha, etc.
- ✅ **Archivos de Resumen**: Descarga automática al finalizar compra
- ✅ **Integración con Usuario**: Requiere login para comprar

---

## 🧪 **DATOS DE PRUEBA - COMPRAS**

### **Usuario de Prueba**
- **Correo**: `test@test.com`
- **Contraseña**: `123456`

### **Compras de Ejemplo Creadas**
```json
// Compra #1
{
  "id": 1,
  "total": 33500,
  "productos": ["Martillo de Acero", "Destornillador Phillips"],
  "estado": "PENDIENTE"
}

// Compra #2  
{
  "id": 2,
  "total": 45000,
  "productos": ["Llave Inglesa 12\"", "Casco de Seguridad"],
  "estado": "PENDIENTE"
}
```

### **Probar el Sistema**
1. **Login**: http://localhost:8080/index.html (usar test@test.com)
2. **Ver Catálogo**: Agregar productos al carrito
3. **Finalizar Compra**: Desde el carrito
4. **Ver Historial**: Verificar que se registró correctamente

---

## 🔧 **RESOLUCIÓN DE PROBLEMAS**

### **Error: Puerto 8080 en uso**
```bash
# Verificar qué proceso usa el puerto
netstat -ano | findstr :8080

# Terminar el proceso
taskkill /PID [NUMERO_PID] /F
```

### **Error de Base de Datos**
- Verificar que PostgreSQL esté ejecutándose
- Comprobar credenciales en `application.properties`
- La base de datos `ferremas` debe existir
- Usuario `ferremas_user` debe tener permisos

### **API no responde**
1. Verificar que la aplicación esté ejecutándose
2. Comprobar en: http://localhost:8080/swagger-ui.html
3. Revisar logs en la consola donde ejecutaste `mvn spring-boot:run`

---

## 📚 **RECURSOS ADICIONALES**

- **Documentación API**: http://localhost:8080/swagger-ui.html
- **Console H2** (desarrollo): http://localhost:8080/h2-console
- **Logs de aplicación**: Visibles en la consola de ejecución
- **Postman Collection**: `Ferremas-API-Postman-Collection.json`

---

## 📞 **SOPORTE**

Para problemas técnicos:
1. Revisar logs de la aplicación
2. Verificar conectividad de base de datos
3. Comprobar que todos los servicios estén ejecutándose
4. Consultar documentación Swagger para endpoints específicos

---

## 💱 **CONVERSIÓN DE MONEDAS**

### **Para Usuarios - Usar el Conversor Web**

#### **1. Acceder al Conversor**
1. Ir a: http://localhost:8080/conversor.html
2. Elegir entre las pestañas disponibles:
   - **Conversor**: Conversión simple entre dos monedas
   - **Tasas de Cambio**: Ver todas las tasas actuales
   - **Conversión Múltiple**: Convertir varios montos a la vez

#### **2. Conversión Simple**
1. Ingresar el monto a convertir
2. Seleccionar moneda de origen (CLP, USD, EUR, UF, UTM)
3. Seleccionar moneda de destino
4. Hacer clic en "Convertir"
5. Ver el resultado con la tasa de cambio utilizada

#### **3. Ver Tasas de Cambio**
1. Ir a la pestaña "Tasas de Cambio"
2. Hacer clic en "Actualizar Tasas" para obtener valores actuales
3. Ver todas las monedas disponibles y sus valores en CLP

#### **4. Conversión Múltiple**
1. Ir a la pestaña "Conversión Múltiple"
2. Ingresar montos separados por comas:
   - **CLP**: Ej: `10000, 50000, 100000`
   - **USD**: Ej: `100, 500, 1000`
3. Hacer clic en "Convertir Todo"
4. Ver todas las conversiones de una vez

### **Para Desarrolladores - API de Conversión**

#### **Obtener todas las monedas disponibles:**
```bash
curl http://localhost:8080/api/divisa/monedas
```

#### **Obtener valor de una moneda específica:**
```bash
curl http://localhost:8080/api/divisa/moneda/USD
curl http://localhost:8080/api/divisa/moneda/EUR
```

#### **Convertir entre cualquier par de monedas:**
```bash
curl -X POST http://localhost:8080/api/divisa/convertir-universal \
  -H "Content-Type: application/json" \
  -d '{
    "monto": 100000,
    "monedaOrigen": "CLP",
    "monedaDestino": "USD"
  }'
```

#### **Conversión múltiple:**
```bash
curl -X POST http://localhost:8080/api/divisa/convertir-multiples \
  -H "Content-Type: application/json" \
  -d '{
    "conversiones": [
      {"monto": 100000, "monedaOrigen": "CLP", "monedaDestino": "USD"},
      {"monto": 500, "monedaOrigen": "USD", "monedaDestino": "CLP"},
      {"monto": 50000, "monedaOrigen": "CLP", "monedaDestino": "EUR"}
    ]
  }'
```

#### **Ver monedas soportadas:**
```bash
curl http://localhost:8080/api/divisa/monedas-soportadas
```

### **💰 Monedas Soportadas**

| Código | Nombre | Símbolo | Descripción |
|--------|--------|---------|-------------|
| **CLP** | Peso Chileno | $ | Moneda base del sistema |
| **USD** | Dólar Estadounidense | US$ | Divisa internacional |
| **EUR** | Euro | € | Moneda europea |
| **UF** | Unidad de Fomento | UF | Unidad de cuenta chilena |
| **UTM** | Unidad Tributaria Mensual | UTM | Unidad tributaria chilena |

**Nota**: Todas las conversiones se realizan usando CLP como moneda base, obteniendo datos en tiempo real del Banco Central de Chile.

---

**¡Listo para usar! 🎉**

Tu API Ferremas está completamente funcional y lista para gestionar el catálogo de productos, usuarios, administración del sistema y ahora también un completo sistema de compras con historial detallado.
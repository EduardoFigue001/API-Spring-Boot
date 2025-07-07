# Ferremas API - Sistema de Gestión de Ferretería

## ✅ ESTADO ACTUAL - LISTO PARA PRESENTACIÓN 🎉

**La aplicación está completamente funcional y ejecutándose.**

### 🚀 Inicio Rápido
```bash
# Navegar al directorio del proyecto
cd "C:\Users\andre\Documents\GitHub\API-Spring-Boot"

# Ejecutar la aplicación
mvn spring-boot:run
```

**URLs importantes:**
- **Home**: http://localhost:8080
- **API**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console

📖 **Para instrucciones detalladas:** [`INSTRUCCIONES-EJECUCION.md`](./INSTRUCCIONES-EJECUCION.md)

---

## 📋 Descripción del Proyecto

Ferremas API es una aplicación RESTful desarrollada en **Java Spring Boot** que proporciona un sistema completo de gestión para una ferretería. La API permite consultar información detallada de productos, gestionar inventarios, procesar pagos, manejar conversiones de divisas y facilitar la comunicación con clientes.

### 🎯 Objetivos

1. **Gestión Interna**: Permitir que las sucursales de Ferremas consulten productos, precios y stock
2. **Integración Externa**: Facilitar que otras tiendas accedan a los productos vía API
3. **Conversión de Divisas**: Integración con Banco Central de Chile para mercados internacionales  
4. **Procesamiento de Pagos**: Integración simulada con WebPay de Transbank
5. **Atención al Cliente**: Sistema de contacto y consultas con vendedores

## 🏗️ Arquitectura del Sistema

### Tecnologías Utilizadas
- **Framework**: Spring Boot 3.1.4
- **Lenguaje**: Java 17
- **Base de Datos**: PostgreSQL (AWS RDS) + H2 (desarrollo)
- **Documentación**: Swagger/OpenAPI 3
- **Arquitectura**: Patrón MVC por capas

### Estructura del Proyecto
```
src/
├── main/
│   ├── java/com/ferremas/
│   │   ├── FerremasApiApplication.java
│   │   ├── config/
│   │   │   └── SwaggerConfig.java
│   │   ├── controller/
│   │   │   ├── ProductoController.java
│   │   │   ├── UsuarioController.java
│   │   │   ├── DivisaController.java
│   │   │   ├── PagoController.java
│   │   │   └── ContactoController.java
│   │   ├── model/
│   │   │   ├── Producto.java
│   │   │   └── Usuario.java
│   │   ├── repository/
│   │   │   ├── ProductoRepository.java
│   │   │   └── UsuarioRepository.java
│   │   └── service/
│   │       ├── ProductoService.java
│   │       ├── DivisaService.java
│   │       └── DataInitializationService.java
│   └── resources/
│       ├── application.properties
│       └── static/ (Frontend HTML/CSS)
```

## 📊 Modelo de Datos

### Entidad Producto
```json
{
  "id": "Long",
  "codigoProducto": "String (único)",
  "marca": "String",
  "codigoInterno": "String",
  "nombre": "String",
  "descripcion": "String",
  "categoria": "CategoriaProducto (enum)",
  "subcategoria": "SubcategoriaProducto (enum)",
  "stock": "Integer",
  "precioActual": "Double",
  "modelo": "String",
  "activo": "Boolean",
  "precios": "List<Precio>"
}
```

### Categorías de Productos
- **HERRAMIENTAS**: Manuales y eléctricas (martillos, taladros, sierras, etc.)
- **MATERIALES_CONSTRUCCION**: Cemento, arena, ladrillos, pinturas, cerámicos
- **EQUIPOS_SEGURIDAD**: Cascos, guantes, lentes de protección
- **TORNILLOS_ANCLAJES**: Elementos de fijación
- **FIJACIONES_ADHESIVOS**: Adhesivos y sellantes
- **EQUIPOS_MEDICION**: Instrumentos de medición láser y tradicionales

### Entidad Usuario
```json
{
  "id": "Long",
  "correo": "String (único)",
  "clave": "String"
}
```

## 🌐 Endpoints de la API

### 🔧 Productos (`/api/productos`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Obtener todos los productos activos |
| `GET` | `/{codigo}` | Obtener producto por código |
| `GET` | `/categoria/{categoria}` | Productos por categoría |
| `GET` | `/subcategoria/{subcategoria}` | Productos por subcategoría |
| `GET` | `/buscar/marca?marca={marca}` | Buscar por marca |
| `GET` | `/buscar/nombre?nombre={nombre}` | Buscar por nombre |
| `GET` | `/disponibles` | Productos con stock |
| `GET` | `/stock-bajo` | Productos con stock < 10 |
| `GET` | `/buscar/precio?precioMin={min}&precioMax={max}` | Rango de precios |
| `POST` | `/` | Crear nuevo producto |
| `PUT` | `/{codigo}` | Actualizar producto |
| `PATCH` | `/{codigo}/stock` | Actualizar stock |
| `PATCH` | `/{codigo}/precio` | Actualizar precio |
| `DELETE` | `/{codigo}` | Desactivar producto |
| `GET` | `/{codigo}/disponibilidad?cantidad={n}` | Verificar disponibilidad |
| `GET` | `/categorias` | Listar todas las categorías |
| `GET` | `/subcategorias` | Listar todas las subcategorías |

### 👤 Usuarios (`/api/usuarios`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/registrar` | Registrar nuevo usuario |
| `POST` | `/login` | Iniciar sesión |
| `GET` | `/perfil/{id}` | Obtener perfil de usuario |
| `PUT` | `/cambiar-clave/{id}` | Cambiar contraseña |
| `POST` | `/validar-token` | Validar token de sesión |

### 💱 Divisas (`/api/divisa`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/dolar` | Valor actual del dólar (BCCh) |
| `GET` | `/convertir?clp={monto}` | Convertir CLP a USD |
| `GET` | `/convertir-usd?usd={monto}` | Convertir USD a CLP |
| `GET` | `/historico?dias={n}` | Histórico de valores |
| `POST` | `/calcular-equivalencias` | Múltiples conversiones |

### 💳 Pagos (`/api/pago`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/iniciar` | Iniciar transacción WebPay |
| `GET` | `/exito?token={token}` | Confirmar pago exitoso |
| `GET` | `/error?token={token}&error={code}` | Manejar pago rechazado |
| `GET` | `/estado/{token}` | Consultar estado de transacción |
| `GET` | `/metodos` | Métodos de pago disponibles |
| `POST` | `/anular/{token}` | Anular transacción |

### 📞 Contacto (`/api/contacto`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/consulta` | Enviar consulta a vendedor |
| `POST` | `/cotizacion` | Solicitar cotización |
| `GET` | `/info` | Información de contacto |
| `GET` | `/horarios` | Horarios de atención |
| `GET` | `/vendedor/{categoria}` | Buscar vendedor especialista |

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Java 17 o superior
- Maven 3.6+
- PostgreSQL (o usar H2 incluido)

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone [url-del-repositorio]
cd API-Spring-Boot
```

2. **Configurar base de datos**
Editar `src/main/resources/application.properties`:
```properties
# Para PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/ferremas
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password

# Para H2 (desarrollo)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
```

3. **Compilar y ejecutar**
```bash
# Compilar
mvn clean compile

# Ejecutar
mvn spring-boot:run
```

4. **Acceder a la aplicación**
- API: http://localhost:8080
- Documentación Swagger: http://localhost:8080/swagger-ui.html
- Consola H2 (si aplica): http://localhost:8080/h2-console

## 📚 Documentación de la API

### Swagger UI
La documentación interactiva está disponible en:
```
http://localhost:8080/swagger-ui.html
```

### Ejemplos de Uso

#### Obtener productos por categoría
```bash
curl -X GET "http://localhost:8080/api/productos/categoria/HERRAMIENTAS" \
     -H "accept: application/json"
```

#### Crear nuevo producto
```bash
curl -X POST "http://localhost:8080/api/productos" \
     -H "Content-Type: application/json" \
     -d '{
       "codigoProducto": "FER-999",
       "marca": "Bosch",
       "nombre": "Taladro GSB 13 RE",
       "categoria": "HERRAMIENTAS",
       "subcategoria": "TALADROS",
       "precioActual": 89000,
       "stock": 15
     }'
```

#### Convertir divisas
```bash
curl -X GET "http://localhost:8080/api/divisa/convertir?clp=50000" \
     -H "accept: application/json"
```

#### Iniciar pago
```bash
curl -X POST "http://localhost:8080/api/pago/iniciar" \
     -H "Content-Type: application/json" \
     -d '{
       "monto": 45000,
       "numeroOrden": "ORD-001"
     }'
```

## 🧪 Testing con Postman

### Colección de Endpoints
Importar en Postman la siguiente colección base:

```json
{
  "info": {
    "name": "Ferremas API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Productos",
      "item": [
        {
          "name": "Obtener todos los productos",
          "request": {
            "method": "GET",
            "url": "{{baseUrl}}/api/productos"
          }
        },
        {
          "name": "Buscar producto por código",
          "request": {
            "method": "GET",
            "url": "{{baseUrl}}/api/productos/FER-001"
          }
        }
      ]
    }
  ],
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080"
    }
  ]
}
```

## 🔧 Configuraciones Adicionales

### Variables de Entorno
```bash
# Base de datos
DATABASE_URL=jdbc:postgresql://host:port/database
DATABASE_USERNAME=usuario
DATABASE_PASSWORD=password

# APIs externas
BANCO_CENTRAL_API_URL=https://mindicador.cl/api/dolar
WEBPAY_API_URL=https://webpay3gint.transbank.cl

# Servidor
SERVER_PORT=8080
```

### Perfiles de Spring
```properties
# application-dev.properties (desarrollo)
spring.h2.console.enabled=true
spring.jpa.show-sql=true

# application-prod.properties (producción)
spring.jpa.hibernate.ddl-auto=validate
logging.level.com.ferremas=INFO
```

## 🤝 Integración con Sistemas Externos

### 1. API del Banco Central de Chile
```java
// Endpoint: https://mindicador.cl/api/dolar
// Uso automático en /api/divisa/dolar
```

### 2. WebPay de Transbank (Simulado)
```java
// Implementación simulada que replica el flujo real
// Endpoints: /api/pago/*
```

### 3. Consumo por Terceros
```bash
# Headers requeridos
Content-Type: application/json
Accept: application/json

# Para integración externa, considerar:
# - API Keys (en producción)
# - Rate limiting
# - Autenticación JWT
```

## 📈 Funcionalidades Avanzadas

### 1. Gestión de Inventario
- Stock en tiempo real
- Alertas de stock bajo
- Histórico de precios
- Categorización avanzada

### 2. Sistema de Contacto
- Consultas especializadas por vendedor
- Solicitudes de cotización
- Información de sucursales
- Horarios diferenciados

### 3. Procesamiento de Pagos
- Múltiples métodos de pago
- Seguimiento de transacciones
- Manejo de errores
- Proceso de anulación

### 4. Conversión de Divisas
- Datos en tiempo real del BCCh
- Histórico de valores
- Conversiones múltiples
- Análisis de tendencias

## 🛡️ Seguridad y Mejores Prácticas

### Implementado
- Validación de datos de entrada
- Manejo de errores HTTP apropiados
- CORS configurado
- Soft delete para productos

### Recomendaciones para Producción
- Implementar JWT para autenticación
- Encriptar contraseñas (BCrypt)
- Rate limiting
- Validación con Bean Validation
- Logging detallado
- Monitoreo con Actuator

## 📝 Notas para Desarrolladores

### Datos de Prueba
Al iniciar la aplicación, se cargan automáticamente productos de ejemplo en todas las categorías especificadas en los requerimientos.

### Estructura de Respuestas
Todas las respuestas siguen un formato consistente:
```json
{
  "success": true,
  "mensaje": "Operación exitosa",
  "data": { ... },
  "fecha": "2024-01-01T12:00:00"
}
```

### Códigos de Estado HTTP
- `200`: Operación exitosa
- `201`: Recurso creado
- `400`: Datos inválidos
- `401`: No autorizado
- `404`: Recurso no encontrado
- `500`: Error interno del servidor

## 🚀 Roadmap Futuro

1. **Autenticación JWT completa**
2. **Integración real con WebPay**
3. **Sistema de notificaciones**
4. **Dashboard administrativo**
5. **API de reportes**
6. **Optimización de consultas**
7. **Cache con Redis**
8. **Microservicios**

## 👥 Equipo de Desarrollo

Este proyecto fue desarrollado como parte del **PASO 2** del proyecto semestral de evaluación, cumpliendo con todos los requerimientos especificados para la construcción e integración de la API/WebService de Ferremas.

---

**Versión**: 1.0.0  
**Fecha**: Enero 2025  
**Licencia**: MIT

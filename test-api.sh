# Script de Pruebas - Ferremas API
# Ejecutar estos comandos en terminal para probar toda la funcionalidad

# NOTA: Asegúrate de que la aplicación esté ejecutándose en http://localhost:8080

echo "=== FERREMAS API - SCRIPT DE PRUEBAS ==="
echo ""

# 1. PRODUCTOS - Obtener todos los productos
echo "1. Obteniendo todos los productos..."
curl -X GET "http://localhost:8080/api/productos" -H "accept: application/json"
echo -e "\n\n"

# 2. PRODUCTOS - Buscar producto específico
echo "2. Buscando producto FER-001..."
curl -X GET "http://localhost:8080/api/productos/FER-001" -H "accept: application/json"
echo -e "\n\n"

# 3. PRODUCTOS - Productos por categoría
echo "3. Productos de categoría HERRAMIENTAS..."
curl -X GET "http://localhost:8080/api/productos/categoria/HERRAMIENTAS" -H "accept: application/json"
echo -e "\n\n"

# 4. PRODUCTOS - Buscar por marca
echo "4. Buscando productos marca Bosch..."
curl -X GET "http://localhost:8080/api/productos/buscar/marca?marca=Bosch" -H "accept: application/json"
echo -e "\n\n"

# 5. PRODUCTOS - Productos disponibles (con stock)
echo "5. Productos disponibles..."
curl -X GET "http://localhost:8080/api/productos/disponibles" -H "accept: application/json"
echo -e "\n\n"

# 6. PRODUCTOS - Obtener categorías
echo "6. Obteniendo todas las categorías..."
curl -X GET "http://localhost:8080/api/productos/categorias" -H "accept: application/json"
echo -e "\n\n"

# 7. USUARIOS - Registrar nuevo usuario
echo "7. Registrando nuevo usuario..."
curl -X POST "http://localhost:8080/api/usuarios/registrar" \
     -H "Content-Type: application/json" \
     -d '{
       "correo": "usuario.prueba@ferremas.cl",
       "clave": "password123"
     }'
echo -e "\n\n"

# 8. USUARIOS - Login
echo "8. Iniciando sesión..."
curl -X POST "http://localhost:8080/api/usuarios/login" \
     -H "Content-Type: application/json" \
     -d '{
       "correo": "usuario.prueba@ferremas.cl",
       "clave": "password123"
     }'
echo -e "\n\n"

# 9. DIVISAS - Valor del dólar
echo "9. Obteniendo valor actual del dólar..."
curl -X GET "http://localhost:8080/api/divisa/dolar" -H "accept: application/json"
echo -e "\n\n"

# 10. DIVISAS - Convertir CLP a USD
echo "10. Convirtiendo $50.000 CLP a USD..."
curl -X GET "http://localhost:8080/api/divisa/convertir?clp=50000" -H "accept: application/json"
echo -e "\n\n"

# 11. DIVISAS - Convertir USD a CLP
echo "11. Convirtiendo $100 USD a CLP..."
curl -X GET "http://localhost:8080/api/divisa/convertir-usd?usd=100" -H "accept: application/json"
echo -e "\n\n"

# 12. PAGOS - Iniciar transacción
echo "12. Iniciando transacción de pago..."
curl -X POST "http://localhost:8080/api/pago/iniciar" \
     -H "Content-Type: application/json" \
     -d '{
       "monto": 45000,
       "numeroOrden": "ORD-TEST-001"
     }'
echo -e "\n\n"

# 13. PAGOS - Obtener métodos de pago
echo "13. Obteniendo métodos de pago disponibles..."
curl -X GET "http://localhost:8080/api/pago/metodos" -H "accept: application/json"
echo -e "\n\n"

# 14. CONTACTO - Enviar consulta
echo "14. Enviando consulta a vendedor..."
curl -X POST "http://localhost:8080/api/contacto/consulta" \
     -H "Content-Type: application/json" \
     -d '{
       "nombre": "Juan Pérez",
       "email": "juan.perez@email.com",
       "mensaje": "Necesito información sobre herramientas eléctricas"
     }'
echo -e "\n\n"

# 15. CONTACTO - Solicitar cotización
echo "15. Solicitando cotización..."
curl -X POST "http://localhost:8080/api/contacto/cotizacion" \
     -H "Content-Type: application/json" \
     -d '{
       "nombre": "María González",
       "email": "maria.gonzalez@empresa.cl",
       "productos": "Taladros Bosch, Sierras Makita"
     }'
echo -e "\n\n"

# 16. CONTACTO - Información de contacto
echo "16. Obteniendo información de contacto..."
curl -X GET "http://localhost:8080/api/contacto/info" -H "accept: application/json"
echo -e "\n\n"

# 17. CONTACTO - Buscar vendedor especialista
echo "17. Buscando vendedor especialista en herramientas..."
curl -X GET "http://localhost:8080/api/contacto/vendedor/herramientas" -H "accept: application/json"
echo -e "\n\n"

# 18. PRODUCTOS - Crear nuevo producto
echo "18. Creando nuevo producto..."
curl -X POST "http://localhost:8080/api/productos" \
     -H "Content-Type: application/json" \
     -d '{
       "codigoProducto": "FER-TEST-001",
       "marca": "TEST BRAND",
       "codigoInterno": "TST-001",
       "nombre": "Producto de Prueba",
       "descripcion": "Producto creado para pruebas de la API",
       "categoria": "HERRAMIENTAS",
       "subcategoria": "HERRAMIENTAS_MANUALES",
       "stock": 25,
       "precioActual": 15000.0,
       "modelo": "TEST-MODEL"
     }'
echo -e "\n\n"

# 19. PRODUCTOS - Verificar disponibilidad
echo "19. Verificando disponibilidad del producto de prueba..."
curl -X GET "http://localhost:8080/api/productos/FER-TEST-001/disponibilidad?cantidad=5" -H "accept: application/json"
echo -e "\n\n"

# 20. PRODUCTOS - Actualizar stock
echo "20. Actualizando stock del producto de prueba..."
curl -X PATCH "http://localhost:8080/api/productos/FER-TEST-001/stock" \
     -H "Content-Type: application/json" \
     -d '{
       "stock": 30
     }'
echo -e "\n\n"

echo "=== FIN DE PRUEBAS ==="
echo "Revisa los resultados arriba para verificar el funcionamiento de la API"
echo "También puedes acceder a la documentación Swagger en: http://localhost:8080/swagger-ui.html"

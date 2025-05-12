# Cities Challenge

## Descripción
Aplicación Android que muestra un listado de ciudades del mundo, permite buscarlas, marcarlas como favoritas y ver su ubicación en Google Maps. La aplicación está diseñada siguiendo los principios de Clean Architecture y MVVM.

## Características principales
- Visualización de ciudades en una lista ordenada alfabéticamente
- Búsqueda de ciudades por prefijo (case insensitive)
- Marcado de ciudades como favoritas
- Filtrado para mostrar solo ciudades favoritas
- Visualización de ubicación en Google Maps
- Pantalla de información detallada de cada ciudad
- Interfaz adaptativa (vista separada en modo retrato, vista combinada en modo paisaje)
- Optimización para búsquedas rápidas usando Room y FTS (Full-Text Search)

## Arquitectura
El proyecto sigue los principios de **Clean Architecture** con tres capas principales:
- **Presentación**: Compose UI, ViewModels
- **Dominio**: Casos de uso, modelos de dominio, interfaces de repositorio
- **Datos**: Implementaciones de repositorio, fuentes de datos (API y local)

## Tecnologías y bibliotecas
- **Kotlin** como lenguaje principal
- **Jetpack Compose** para la UI
- **Coroutines** y **Flow** para operaciones asíncronas
- **Hilt** para inyección de dependencias
- **Retrofit** para comunicación con la API
- **Room** para persistencia local
- **Google Maps** para visualización de mapas
- **JUnit** y **Mockito** para pruebas unitarias

## Patrones de diseño utilizados
- **MVVM** (Model-View-ViewModel)
- **Repository Pattern**
- **Use Cases**
- **Dependency Injection**
- **Factory Pattern** (para creación de ViewModels en previews)

## Configuración del proyecto
1. Clonar el repositorio
2. Añadir tu API Key de Google Maps en el archivo `local.properties`:
   ```
   MAPS_API_KEY=tu_api_key_aquí
   ```
3. Sincronizar el proyecto con Gradle
4. Ejecutar la aplicación

## Funcionalidades destacadas
- **Búsqueda optimizada**: Implementación eficiente con índices en SQLite y filtrado por prefijo
- **Interfaz adaptativa**: Diseño que se ajusta a la orientación del dispositivo
- **Previsualización de Composables**: Funcionalidad para previsualizar componentes sin depender de Hilt
- **Gestión de estados UI**: Implementación robusta de estados (Loading, Error, Success, Empty)
- **Manejo centralizado de errores**: Sistema unificado para mensajes de error

## Test
El proyecto incluye pruebas unitarias para:
- Modelos de datos
- Repositorios
- Casos de uso
- Algoritmo de búsqueda

## Posibles mejoras futuras
- Añadir animaciones más elaboradas
- Añadir soporte para múltiples idiomas
- Añadir test para ViewModels
- Añadir un sistema de notificaciones para actualizaciones de ciudades
- Implementar caché con expiración para los datos
- Implementar modo oscuro
- Implementar pruebas de integración y UI tests con Espresso
- Implementar un sistema de autenticación para usuarios

## Screenshots
[Agregar capturas de pantalla de la aplicación aquí]

---

# KTS Android KMP Project
Это демонстрационный проект, созданный с использованием **Kotlin Multiplatform** и **Compose Multiplatform**, который работает на платформах **Android** и **iOS**. Приложение представляет собой магазин автомобилей с пробегом

## Текущая функциональность

### Добашняя работа №2

Проект представляет собой простое двухэкранное приложение: приветственный экран и экран входа

## Технологический стек

- **Kotlin Multiplatform**: Общая кодовая база для бизнес-логики и UI
- **Compose Multiplatform**: Создание декларативного UI для Android и iOS из общего кода
- **Навигация**: [Jetpack Navigation for Compose](https://developer.android.com/jetpack/compose/navigation) для переключения между экранами
- **Загрузка изображений**: [Coil3](https://coil-kt.github.io/coil/compose/) для асинхронной загрузки изображений из сети
- **Логирование**: [Napier](https://github.com/AAkira/Napier) для мультиплатформенного логирования
- **Архитектура**: UI-архитектура (Single activity) с разделением на экраны (`screens`), переиспользуемые компоненты (`components`) и навигацию (`navigation`)

## Структура проекта

- `composeApp/`: Основной мультиплатформенный модуль
  - `src/commonMain/kotlin`: Общий код для Android и iOS, включая UI (экраны, компоненты), навигацию и бизнес-логику
  - `src/androidMain/kotlin`: Специфичный код для платформы Android
  - `src/iosMain/kotlin`: Специфичный код для платформы iOS
- `iosApp/`: Точка входа и конфигурация для iOS-приложения

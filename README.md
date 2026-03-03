# KTS Android KMP Project

Демонстрационный учебный проект на **Kotlin Multiplatform (KMP)** и **Compose Multiplatform**.

## Тема проекта
**VK Profile Companion** (условная соц. сеть / мини‑клон VK: экран логина + главная лента постов).

## Функциональность (по текущему состоянию)
- Экран приветствия (Intro)
- Экран логина
  - MVVM, `StateFlow`‑стейт экрана (`LoginUiState`)
  - события навигации через `SharedFlow` (успешный/ошибочный логин)
- Главный экран (Main)
  - Лента постов (`LazyColumn` + `key`)
  - загрузка изображений из сети через **Coil 3**
  - обработка Back: с главного экрана нельзя вернуться назад (double‑back‑to‑exit)

## Технологии
- Kotlin Multiplatform
- Compose Multiplatform
- Navigation: Jetpack Navigation for Compose
- Изображения: Coil 3 (`coil3-compose`, сеть через OkHttp на Android)
- Логирование: Napier
- Архитектура: MVVM

## Структура проекта (актуально)
Проект содержит **один gradle‑модуль**:

- `:composeApp/` — основной модуль
  - `src/commonMain/` — общий код (Compose UI, экранные компоненты, ViewModel/State, навигация, utils)
  - `src/androidMain/` — Android‑специфичный код (например, BackHandler и т.п.)
  - `src/iosMain/`, `src/iosArm64Main/`, `src/iosSimulatorArm64Main/` — iOS‑специфичные исходники (если используются)
  - `build.gradle.kts` — KMP targets + Android application конфигурация

Дополнительно:
- `iosApp/` — Xcode‑проект (обвязка под iOS)
- `gradle/` — версии/каталог зависимостей + wrapper

## Пакеты в `commonMain`
Код в `composeApp/src/commonMain/kotlin` — общий для платформ. Основные пакеты:

- `com.example.kts_android_kmp.app`
  - точка входа в UI (`App`) и корневая навигация (`AppNavigation`)

- `com.example.kts_android_kmp.feature.*`
  - фичи приложения (экраны и логика), каждая фича живёт в своём пакете
  - примеры:
    - `feature.intro` — экран приветствия
    - `feature.login` — экран логина
    - `feature.main` — главный экран с лентой

- `com.example.kts_android_kmp.theme`
  - дизайн‑система приложения: цвета, размеры/отступы (`Dimens`), константы оформления

- `com.example.kts_android_kmp.utils`
  - переиспользуемые UI/технические утилиты

- `com.example.kts_android_kmp.common`
  - базовые абстракции, которые используются в разных фичах

- `com.example.kts_android_kmp.platform`
  - мультиплатформенные API через `expect/actual`

### Ресурсы (строки/картинки)
В проекте используется Compose Resources (генерируется объект `Res`).
Строки берутся как `stringResource(Res.string.some_key)`.
Это работает в `commonMain` и позволяет не дублировать `strings.xml` на разных платформах.

> Репозиторий сейчас собран вокруг одного модуля `:composeApp`, который содержит общий UI/логику и Android-приложение.
> iOS-часть собирается как `framework` из KMP (см. `iosArm64/iosSimulatorArm64` в gradle).

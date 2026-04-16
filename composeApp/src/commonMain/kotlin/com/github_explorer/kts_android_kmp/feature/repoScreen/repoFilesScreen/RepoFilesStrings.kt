package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen

object RepoFilesStrings {
    const val ENTER_FILE_NAME = "Введите имя файла"
    const val INVALID_FILE_NAME = "Некорректное имя файла"
    const val FILE_CREATED = "Файл создан: "
    const val UPLOAD_ERROR = "Ошибка загрузки"
    const val FILE_NOT_LOADED = "Файл еще не загружен"
    const val EDIT_ONLY_TEXT = "Редактирование доступно только для CODE/JSON/TEXT"
    const val FILE_UPDATED = "Файл обновлен"
    const val UPDATE_ERROR = "Ошибка обновления"
    const val PATH_NOT_DEFINED = "Путь файла не определен"

    const val CONFLICT_ERROR =
        "Конфликт: файл изменился на сервере (sha mismatch). Обновите данные и попробуйте снова."
    const val VALIDATION_ERROR =
        "Ошибка валидации (422). Возможно, файл уже существует или путь/имя некорректны."
    const val UNAUTHORIZED_ERROR = "Не авторизован (401)."
    const val FORBIDDEN_ERROR = "Нет доступа (403). Проверьте права/скоупы токена."
    const val NOT_FOUND_ERROR = "Ресурс не найден (404)."
    const val SERVER_ERROR = "Ошибка сервера GitHub: "
}

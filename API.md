# Документация API

## Файлы

### Получить файл
- Метод: GET
- Путь: /v0/files/{id}
- Параметры:
  - id (UUID) - идентификатор файла
- Возвращает: файл в виде байтов с соответствующим Content-Type

---

## Профиль

### Получить свой профиль
- Метод: GET  
- Путь: /v0/profile
- Требует: аутентификации
- Возвращает: ProfileResponseDTO - данные профиля текущего пользователя

### Получить профиль по handle
- Метод: GET
- Путь: /v0/profile/{handle}
- Параметры:
  - handle (String) - уникальный идентификатор профиля
- Возвращает: ProfileResponseDTO - данные профиля

### Обновить аватар
- Метод: POST
- Путь: /v0/profile/avatar
- Требует: аутентификации
- Параметры:
  - file (MultipartFile) - файл изображения
- Возвращает: ProfileResponseDTO - обновленный профиль

### Обновить профиль
- Метод: PUT
- Путь: /v0/profile
- Требует: аутентификации
- Принимает: ProfileUpdateDTO - новые данные профиля
- Возвращает: ProfileResponseDTO - обновленный профиль

---

## Курсы (CourseController)

### Получить все курсы
- Метод: GET
- Путь: /v0/course
- Возвращает: список CourseDTO - все курсы

### Создать курс
- Метод: POST
- Путь: /v0/course
- Параметры:
  - course (CreateCourseDTO) - данные курса
  - preview (MultipartFile, опционально) - превью изображение
- Возвращает: CourseDTO - созданный курс

### Получить курс по ID
- Метод: GET
- Путь: /v0/course/id/{id}
- Параметры:
  - id (Long) - идентификатор курса
- Возвращает: CourseDTO - данные курса

### Получить курс по handle
- Метод: GET
- Путь: /v0/course/handle/{handle}
- Параметры:
  - handle (String) - уникальный идентификатор курса
- Возвращает: CourseDTO - данные курса

### Обновить превью курса
- Метод: POST
- Путь: /v0/course/{id}/preview
- Параметры:
  - id (Long) - идентификатор курса
  - file (MultipartFile) - файл изображения
- Возвращает: CourseDTO - обновленный курс

### Удалить курс
- Метод: DELETE
- Путь: /v0/course/{id}
- Параметры:
  - id (Long) - идентификатор курса
- Возвращает: статус успешного удаления

---

## Занятия (LessonController)

### Создать занятие
- Метод: POST
- Путь: /v0/lessons/{courseId}
- Параметры:
  - courseId (Long) - идентификатор курса
- Принимает: CreateLessonDTO - данные занятия
- Возвращает: LessonDTO - созданное занятие

### Получить занятие по ID
- Метод: GET
- Путь: /v0/lessons/{lessonId}
- Параметры:
  - lessonId (Long) - идентификатор занятия
- Возвращает: LessonDTO - данные занятия

### Получить занятия курса
- Метод: GET
- Путь: /v0/lessons/course/{courseId}
- Параметры:
  - courseId (Long) - идентификатор курса
- Возвращает: список LessonDTO - все занятия курса

### Удалить занятие
- Метод: DELETE
- Путь: /v0/lessons/{lessonId}
- Параметры:
  - lessonId (Long) - идентификатор занятия
- Возвращает: статус успешного удаления

---

## DTO модели

### ProfileResponseDTO
- fullName - полное имя
- summary - описание профиля
- handle - уникальный идентификатор
- avatarUrl - URL аватара

### ProfileUpdateDTO
- fullName - полное имя
- summary - описание профиля
- handle - уникальный идентификатор

### CourseDTO
- id - идентификатор курса
- name - название курса
- description - описание курса
- tags - список тегов
- handle - уникальный идентификатор
- previewUrl - URL превью

### CreateCourseDTO
- name - название курса
- description - описание курса
- tags - набор тегов

### LessonDTO
- id - идентификатор занятия
- name - название занятия
- description - описание занятия
- beginAt - время начала
- endsAt - время окончания
- state - статус занятия
- courseId - ID курса

### CreateLessonDTO
- name - название занятия
- description - описание занятия
- beginAt - время начала
- endsAt - время окончания

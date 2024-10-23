@startuml
actor Пользователь
participant API

Пользователь -> API: POST /api/listings/create
API -> Database: Проверить данные
Database --> API: Подтверждение корректности
API -> Пользователь: 201 Created, ID объявления
@enduml

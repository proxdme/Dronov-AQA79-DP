# Git. README.md (пример)

# Автоматизация тестирования веб-сервиса покупки туров

Веб-сервис, который предлагает купить тур по определённой цене двумя способами: обычная оплата по дебетовой карте и уникальная технология: выдача кредита по данным банковской карты.

## Начало работы

Небольшой набор инструкций, объясняющий, как получить копию этого проекта для запуска на локальном ПК.

1. основные элементы проекта храняться в удаленном репозитории на GitHub: https://github.com/proxdme/Dronov-AQA79-DP
2. для запуска проекта необходимо на компьютере под управлением ОС Windows 11.0 Домашняя 21H2 сборка 22000.2538 установить несколько приложений: IntelliJ IDEA, браузер Chrome, Docker Desktop, DBeaver, VS Code, Java 11, Git 
3. после установки приложений, запустить Docker Desktop, IntelliJ IDEA, 
4. 




### Prerequisites

Что нужно установить на ПК для использования (например: Git, браузер и т.д.).
IntelliJ IDEA 2024.1 (Community Edition) Build #IC-241.14494.240, built on March 28, 2024
Chrome Версия 125.0.6422.142 (Официальная сборка), (64 бит)
Docker Desktop 4.30.0 (149282)
DBeaver версия 24.1.0.202406021658
Visual Studio Code (VS Code)
Git c https://git-scm.com/downloads
```
Примеры
```

### Установка и запуск

Пошаговый процесс установки и запуска

1. в терминале IntelliJ ввести команду docker-compose up --build
2. в терминале IntelliJ ввести команду java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar для mysql
3. java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar для postgresql 
   


```
Примеры
```

## Лицензия

Опишите условия лицензии

Windows 11.0 Домашняя 21H2 сборка 22000.2538
IntelliJ IDEA 2024.1 (Community Edition)
Build #IC-241.14494.240, built on March 28, 2024
Chrome Версия 125.0.6422.142 (Официальная сборка), (64 бит)
Docker Desktop 4.30.0 (149282)
DBeaver версия 24.1.0.202406021658
Visual Studio Code (VS Code)

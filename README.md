## 일단 - 일일 계획을 단순하게
| Type | Link |
| --- | --- |
| 플레이 스토어 | https://play.google.com/store/apps/details?id=com.poptato.app |
| 인스타그램 | https://www.instagram.com/illdan_poptato?igsh=MTR1bmQ2aXU0d2txbA== |
| Linktree | https://linktr.ee/illdan |

## Tech Stack
- Jetpack Compose
- Clean Architecture
- Multi-Module
- MVVM
- Dagger Hilt
- Coroutine
- Custom Plugin(Kotlin-DSL)
- Version Catalog
- Flow
- Data Store
- Navigation
- Coil Compose
- Retrofit2, okHttp3

## Architecture
```
├── app
|   ├── app
|   └── di
│
├── build-logic
|   └── plugin
│
├── data
|   ├── service
|   ├── base
|   ├── model
|   ├── datastore
|   ├── mapper
|   └── repository
│
├── core
│   ├── ui
│   │   ├── base
│   │   ├── common
│   │   └── util
│   ├── main
│   │   ├── base
│   │   ├── enums
│   │   └── util
│   └── navigation
│       └── navigation
│
├── domain
│   ├── base
│   ├── model
│   ├── repository
│   └── usecase
│
├── design-system
│   └── design-system
│
└── feature
    ├── backlog
    ├── category
    ├── history
    ├── login
    ├── mypage
    ├── onboarding
    ├── setting
    ├── splash
    ├── today
    └── yesterdaylist
```

# SafeTravel

이 프로젝트는 Jetpack Compose를 사용하여 Kotlin으로 개발된 모바일 애플리케이션입니다. <br />
이 앱은 Bluetooth 3.0을 통해 하드웨어 구성 요소에 안전하게 연결하여 가방을 잠금 해제하도록 설계되었습니다. <br />
생체 인식 로그인, 사용자 지정 PIN 로그인, 여러 Bluetooth 장치를 관리하기 위한 사용자 친화적인 인터페이스가 특징입니다.<br />

## A 기능
**• 생체 인식 및 PIN 로그인:** Android의 생체 인식 인증 및 사용자 지정 PIN 시스템을 사용하여 안전하게 로그인합니다.<br />
**• Bluetooth 연결:** Bluetooth 3.0 하드웨어 구성 요소에 연결하여 가방을 잠금 해제합니다.<br />
**• 장치 관리:** 앱에서 직접 여러 Bluetooth 장치를 추가, 사용자 지정 및 관리합니다.<br />
**• 사용자 지정 장치:** 쉽게 식별할 수 있도록 장치 아이콘과 이름을 변경합니다.<br />
**• 잠금 해제 기능:** 각 장치에는 잠금 시스템에 메시지를 트리거하는 잠금 해제 버튼이 있습니다.<br />
**• 자동 재연결:** 앱이 포그라운드로 전환되면 자동으로 연결을 설정합니다.<br />
**• 오류 처리:** 모든 잠재적 오류, 부여되지 않은 권한 및 Bluetooth가 꺼진 것과 같은 불법 상태를 관리합니다.<br />
**• 권한 처리:** 시작 시 강력한 권한 요청 시스템.<br />
**• 고급 UI 디자인:** Material 3 디자인 가이드라인을 준수하여 Jetpack Compose를 사용하여 잘 만들어진 UI 디자인.<br />

## 개발된 기술
**•Bluetooth 통신:** Bluetooth 3.0 기기와 연결하고 통신하는 데 대한 전문 지식.<br />
**•권한 처리:** Android 권한을 효과적으로 관리하고 요청하는 데 능숙함.<br />
**•오류 처리:** 다양한 오류 상태를 처리하고 원활한 사용자 경험을 보장하는 기술을 개발함.<br />
**•데이터베이스 관리:** 효율적인 데이터베이스 등록 및 관리를 위해 Room을 사용함.<br />
**•인증 시스템:** 안전한 액세스를 위해 생체 인식 및 사용자 지정 PIN 로그인 시스템을 구현함.<br />
**•고급 UI 디자인:** Jetpack Compose를 사용하여 고급스럽고 시각적으로 매력적인 UI를 만듦.<br />
**•우수한 아키텍처:** 유지 관리 및 확장성을 위해 아키텍처 디자인의 모범 사례를 적용함.<br />
**•종속성 주입:** 효율적인 종속성 주입을 위해 Koin을 활용했습니다.<br />
**•Material 3 테마:** Material 3 디자인 시스템과 그 구성 요소에 대한 강력한 이해를 보여주었습니다.<br />

# SafeTravel

This project is a mobile application developed in Kotlin using Jetpack Compose. <br />
The app is designed to securely connect to a hardware component via Bluetooth 3.0 to unlock a suitcase. <br />
It features biometric login, custom PIN login, and a user-friendly interface for managing multiple Bluetooth devices.<br />

## A Features
**• Biometrical and PIN Login:** Secure login using Android's biometric authentication and a custom PIN system.<br />
**• Bluetooth Connectivity:** Connects to Bluetooth 3.0 hardware components to unlock a suitcase.<br />
**• Device Management:** Add, customize, and manage multiple Bluetooth devices directly from the app.<br />
**• Customizable Devices:** Change device icons and names for easy identification.<br />
**• Unlock Functionality:** Each device has an unlock button that triggers a message to the locking system.<br />
**• Automatic Reconnection:** Automatically establishes connections when the app comes to the foreground.<br />
**• Error Handling:** Manages all potential errors, ungranted permissions, and illegal states like Bluetooth being off.<br />
**• Permission Handling:** Implements a robust permission request system upon startup.<br />
**• Advanced UI Design:** Well-crafted UI designed using Jetpack Compose, adhering to Material 3 design guidelines.<br />

## A Skills Developed 
**•Bluetooth Communication:** Expertise in connecting and communicating with Bluetooth 3.0 devices.<br />
**•Permission Handling:** Proficiency in managing and requesting Android permissions effectively.<br />
**•Error Handling:** Developed skills in handling various error states and ensuring a smooth user experience.<br />
**•Database Management:** Used Room for efficient database registration and management.<br />
**•Authentication Systems:** Implemented biometric and custom PIN login systems for secure access.<br />
**•Advanced UI Design:** Created an advanced and visually appealing UI using Jetpack Compose.<br />
**•Good Architecture:** Applied best practices in architecture design for maintainability and scalability.<br />
**•Dependency Injection:** Utilized Koin for efficient dependency injection.<br />
**•Material 3 Theming:** Demonstrated a strong grasp of the Material 3 design system and its components.<br />

<p float="left">
  <img src="https://github.com/SemenciucCosmin/SafeTravel/blob/main/screenshots/SafeTravel_Light_Authentication.jpeg" width="180">
  <img src="https://github.com/SemenciucCosmin/SafeTravel/blob/main/screenshots/SafeTravel_Light_Devices_List.jpeg" width="180">
  <img src="https://github.com/SemenciucCosmin/SafeTravel/blob/main/screenshots/SafeTravel_Light_Customize.jpeg" width="180">
  <img src="https://github.com/SemenciucCosmin/SafeTravel/blob/main/screenshots/SafeTravel_Light_Add_Device.jpeg" width="180">
</p>

<p float="left">
  <img src="https://github.com/SemenciucCosmin/SafeTravel/blob/main/screenshots/SafeTravel_Dark_Authentication.jpeg" width="180">
  <img src="https://github.com/SemenciucCosmin/SafeTravel/blob/main/screenshots/SafeTravel_Dark_Devices_List.jpeg" width="180">
  <img src="https://github.com/SemenciucCosmin/SafeTravel/blob/main/screenshots/SafeTravel_Dark_Customize.jpeg" width="180">
  <img src="https://github.com/SemenciucCosmin/SafeTravel/blob/main/screenshots/SafeTravel_Dark_Add_Device.jpeg" width="180">
</p>

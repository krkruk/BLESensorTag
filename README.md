BLESensorTag
============
BLESensorTag is an Android app that allows receiving, displaying and storing incoming data from TI's CC2650 SensorTag and other BLE devices.

The app has the following SensorTag-related functionalities:
* Receive data from all available sensors
* Receive Pulse Wave Velocity data thanks to an attached piezoelectric sensor to the SensorTag
* Present incoming data while connected to the BLE
* Store sensor data into SQLite database
* Display stored sensor data
* Export data to CSV file
* Analyze PWV e.g. finding R-R (pulse) including filtering

Additionally, the app provides several BLE profiles according to SIG:
* Generic Access - 0x1800
* Device Information Service - 0x180a
* Heart Rate - 0x180d
* Simple Keys - 0xffe0

The application can be easilly extended to a new set of profiles. Just follow the code.

Additional libraries
====================
The presentation part of the application relies heavily on a 3rd party library.
Special thanks to Philipp Jahoda for his great library [**MPAndroidChart**] (https://github.com/PhilJay/MPAndroidChart).

Contact and bug report
======================
You can contact me via github.

More
====
The app is created as a bachelor in engineering degree project. 

License
=======
Copyright 2016-2017 Krzysztof Kruk

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

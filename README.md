# Taps-to-Trips app
Spring Boot web application that provides API for Trips calculation based on Taps data.


### Properties description
There are several properties for app customization in
`application.properties` file in `src/main/resources/`.

###### resource.dir
Defines path to the folder with resources.  
Default: `src/main/resources/data/`

###### trips.cost.filename
Defines name of the file with information about trip cost between two stops.  
Note: must be in `json` format.  
Default: `tripsCost.json`

###### taps.filename
Defines name of the file with information about taps that should be used for trips calculation.  
Note: must be in `csv` format.  
Default: `taps.csv`

###### trips.filename
Defines name of the file with information about calculated trips.  
Note: must be in `csv` format.  
Default: `trips.csv`


### Requirements
###### trips cost
To make app work properly you must:

- have valid data about trip cost between two stops in `trips.cost.filename` in `resource.dir`
  - this file must be in `json` format with the following structure:
```json
[
  {
    "from": "Stop1",
    "to": "Stop2",
    "cost": 3.25
  }
]
```

###### taps file
To allow app calculate trips based on data in local storage you must:
- include a file `taps.filename` with taps info that will be used for trips calculation in 
  `resource.dir`
    - this file must be in `csv` format with the following header:
```
ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN
```


### Available endpoints

`GET http://localhost:8008/trip` 

Reads a [file](#taps-file) with Taps data (`taps.filename`) from resource directory (`resource.dir`) and write the file 
with Trips 
data (`taps.filename`) there.

###### trips file

This file is in `csv` format with the following header:
```
Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status
```

`POST http://localhost:8008/trip`  

Downloads a [file](#taps-file) with Taps data.  
Returns a [file](#trips-file) with calculated Trips data.

###### Request headers

    Content-Type: multipart/form-data

###### Request body

    Content-Disposition: form-data; name="taps"  
    Content-Type: text/csv; charset=UTF-8


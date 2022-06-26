# Taps-to-Trips app
Spring Boot web application that provides API for Trips calculation based on Taps data.

###### Table of Contents
- [Properties description](#properties-description)
- [App requirements](#app-requirements)
- [Available endpoints](#available-endpoints)
- [How to run](#how-to-run)


- [Calculation algorithm](#calculation-algorithm)
  - [Status calculation](#status-calculation)
  - [Charge calculation](#charge-calculation)
  - [Assumptions](#assumptions)

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


### App requirements
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

### How to run
The app can be either run from IDE on localhost as Spring Boot project or built as jar and deployed on server.  
The calculation is performed by sending one of the described [HTTP requests](#available-endpoints).

## Calculation algorithm

After reading Taps records from the file they are sorted by date.  
Then each record is iterated. If it is:
- Tap-ON - it is put in the stack.
- Tap-OFF - the last record in stack is retrieved and converted altogether with the current record to COMPLETED trip.  

When iteration is finished, there are only Taps of INCOMPLETE trips in the stack. So, they are created.  
Then for each Trip status, duration and charge are calculated.  
Finally, trips are sorted by date.

### Status calculation

- If Trip does not have Finished datetime, it is INCOMPLETE.
- If Trip's FromStop is equal to Trip's ToStop, it is CANCELLED.

### Charge calculation

Charges from the [trips cost configuration file](#trips-cost) are parsed into a Map.  
The charge for COMPLETED trip between two stops is retrieved by stopIDs.  
The charge for INCOMPLETE trip is calculated by finding the max value by fromStopID.

### Assumptions

- `CompanyID, BusID, PAN ` are always equal for matched Tap-ON and Tap-OFF records.
- If a passenger forgot to make a Tap-OFF, their next Tap would be a Tap-ON, assuming that the system recognises 
  Tap-ONs and Tap-OFFs.
- For INCOMPLETE trips the following data is absent from the output result:
  - Finished
  - ToStopId
  - DurationSecs

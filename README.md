
# Exam

The purpose of the code is to visualize the assigned data-set and create a set of classes that represent it.  
(The UML representations are in the bottom of this file).  
Our data-set describes a list of pharmacies.  
The JAVA application has the following features:

**On start-up**: download the data-set (zip or csv) which contains data in CSV format starting from the address provided after decoding the JSON containing the URL useful for downloading the file. This phase will come performed when the application is first started.

This is done by reading from a external configuration file the url and the name of the data-set destination file.  
The JSON contained in the url is then scanned by the class  _JSONParser_  that identify the location of the second url, allowing the download the data-set.  
There is a file presence check that can prevent to download an existing file.

**When the download is completed**: parse the data by creating appropriate data structures based on the above classes (each data-set record corresponds to an object of a class).

The class  _CSVParser_  is used to read and parse the CSV file and to fill a vector of  _Pharmacy_  and one of  _Metadata_.  
Each row represents a  _Pharmacy_  and each field of columns represents a  _Metadata_.  
A pharmacy is described by the following fields:

-   id (CODICEIDENTIFICATIVOSITO)
-   name (DENOMINAZIONESITOLOGISTICO)
-   address (INDIRIZZO)
-   vatNumber (PARTITAIVA)
-   postalCode (CAP)
-   cityCode (CODICECOMUNEISTAT)
-   city (DESCRIZIONECOMUNE)
-   provinceCode (CODICEPROVINCIAISTAT)
-   provinceAbbreviation (SIGLAPROVINCIA)
-   provinceName (DESCRIZIONEPROVINCIA)
-   regionCode (CODICEREGIONE)
-   regionName (DESCRIZIONEREGIONE)
-   beginValidity (DATAINIZIOVALIDITA)
-   endValidity (DATAFINEVALIDITA)
-   latitude (LATITUDINE)
-   longitude (LONGITUDINE)
-   localize (LOCALIZE)

There are three cases of misspelled fields that needs to be corrected manually.

```
public String lineCorrection(String str) {
		String str2 = str;
		if (str.contains("\"Via Cappuccini ;163\"")) {
			str2 = str.replace("\"Via Cappuccini ;163\"", "\"Via Cappuccini ,163\"");
		}
		if (str.contains("\"via vespucci;26\"")) {
			str2 = str.replace("\"via vespucci;26\"", "\"via vespucci,26\"");
		}
		if (str.contains("\'9,258444")) {
			str2 = str.replace("\'9,258444", "9.258444");
		}
		return str2;
	}

```

We also corrected the values of latitude and longitude, replacing the comma with the point and the not allowed values with “-360”.

```
	public String[] coordinateCorrection(String[] str) {
		String[] str2 = str;

		for (int i = 14; i <= 15; i++) {
			if (str[i].contains(","))
				str2[i] = str[i].replace(',', '.');

			if (str[i].equals("-"))
				str2[i] = str[i].replace("-", "-360");
			if (str[i].equals("0"))
				str2[i] = str[i].replace("0", "-360");
		}
		return str2;
	}

```

**On request**: return statistics and filtered dataset using API REST GET or POST.  
Afterwards the various requests that can be carried out with relevant examples will be listed.  
The examples refer to the query or to the body of the POST (JSON).  
The code quoted is not explanatory of the project but serves to give an idea of ​​the reasoning behind.

## GET requests:

### /data

Returns all the data-set.

### /metadata

Returns all the fields.

### [](https://github.com/MatteoOrlandini/ProgrammazioneadOggetti/blob/2a1e7ee9a6c8554c0e448b6cc16a4591d3a040fe/README.md#statsfieldname)**/stats/{fieldName}**

Gives statistics on numbers based on the class  _NumberStats_:

-   Average
-   Minimum
-   Maximum
-   Standard Deviation
-   Sum

The fields on which it makes sense to carry out statistics are only latitude and longitude.  
All the stats are calculated in a single for cycle:

```
    int count = store.size();
	double avg = 0;
	double min = store.get(0);
	double max = store.get(0);
	double std = 0;
	double sum = 0;
	for (Double item : store) {
		avg += item;
		if (item < min)
			min = item;
		if (item > max)
			max = item;
		sum += item;
		std += item * item;
	}
	avg = avg / count;
	std = Math.sqrt((count * std - sum * sum) / (count * count));
	NumberStats stats = new NumberStats(avg, min, max, std, sum);
	return stats;

```

To do it standard deviation is calulated with:  
$$ σ_x={1\over N}\sqrt{N\sum_{i=1}^N x_i^2−\bigg(\sum_{i=1}^N x_i\bigg)^2​} $$

_example:_

> localhost:8080/stats/longitude

_response:_

```
{
    "avg": 12.560673320226854,
    "min": 6.731547284806,
    "max": 18.492467,
    "std": 2.788876305175012,
    "sum": 131108.3081165279
}

```
Note that in this request the code does not use fields with incorrect latitude or longitude (equals to -360).

### count/{fieldName}

Returns the number of times the string of the specified field occourred.  
It needs a field name in the query path and a value to confront in the query params.

```
(@PathVariable String fieldName, @RequestParam(value = "value") String value)

```

_example:_

> localhost:8080/count/beginValidity?value=01/10/2006

_response_

> count : 9

## POST requests:

### /localize

Using a POST request it looks in the body for a latitude, a longitude and a range (in Km) and returns those that are in the specified area.

The formula used to calculate the distance is:

$p1=(lon1,lat1)$ longitude and latitude in radians.  
$p2=(lon2,lat2)$ longitude and latitude in radians.

$dist=arccos(sin(lat1)∗sin(lat2)+cos(lat1)∗cos(lat2)∗cos(lon2−lon1))∗6371$

(6371 in the Earth radius in Km; dist is expressed in Km).

_example:_

```
{
    "latitude": 41.55,
    "longitude": 15.22,
    "range": 10
}

```
Note that in the POST /localize   
the code does not use fields with incorrect latitude or longitude (equals to -360).

### /filter

Generic filter using a POST. If the body of the JSON is a single object it searches for a field, an operator and an input value and returns the filtered dataset. If it is found an attribute called “\$or” or “\$and” it applies multiple filters to the following array of object based on the attribute. The “\$or” filter does a filter for each object and then unites them. The “$and” filter just recursively filter the result of the previous decimation.

or code:

```
for (Object obj : jsonArray) {
				filterParam.readFields(obj);
				tempOr = pharmacyService.filter(pharmacyService.getPharmacies(), filterParam);
				for (Pharmacy item : tempOr) {
					if (!temp.contains(item))
						temp.add(item);
				} 		
	}

```

and code:

```
for (Object obj : jsonArray) {
					filterParam.readFields(obj);
					// iteration on .filter
					temp = pharmacyService.filter(temp, filterParam);		

```

_examples:_

```
{
    "fieldName": "city",
    "operator": "==",
    "value": "Ancona"
}

```

```
{
    "$or": [
        {
            "fieldName": "latitude",
            "operator": "<=",
            "value": 43.1
        },
        {
            "fieldName": "provinceName",
            "operator": "==",
            "value": "Ancona"
        }
    ]
}

```

```
{
    "$and": [
        {
            "fieldName": "postalCode",
            "operator": "==",
            "value": "28021"
        },
        {
            "fieldName": "beginValidity",
            "operator": "<",
            "value": "02/05/2015"
        }
    ]
}

```

```
{
    "$and": [
        {
            "fieldName": "regionName",
            "operator": "==",
            "value": "Puglia"
        },
        {
            "fieldName": "endValidity",
            "operator": ">",
            "value": "20/04/2012"
        },
        {
            "fieldName": "endValidity",
            "operator": "<",
            "value": "20/05/2012"
        }
    ]
}

```

### /filter/stats/{fieldName}

Gives stats like the request “/stats/{fieldName}” but on a sample made of the filtrated pharmacies.  
In the body we put the specifics of the filter like with “/filter” and in the query there must be latitude or longitude.

_example:_

> localhost:8080/filter/stats/latitude

```
{
    "$and": [
        {
            "fieldName": "city",
            "operator": "==",
            "value": "Bari"
        },
        {
            "fieldName": "beginValidity",
            "operator": ">",
            "value": "01/01/2012"
        },
        {
            "fieldName": "beginValidity",
            "operator": "<",
            "value": "31/12/2012"
        }
    ]
}

```

_response:_

```
{
    "avg": 41.09908327509544,
    "min": 41.0453375512566,
    "max": 41.1256631,
    "std": 0.02907812314007932,
    "sum": 287.69358292566807
}

```

## Error managing
This application notifies the user if there are some errors in the various requests. 

 - Filter features

If there is a wrong field name in a filter request like

>  localhost:8080/filter/

    {
        "fieldName": "cty",
        "operator": "==",
        "value": "Bari" 
    }
    
The application returns this message because there is not a "cty" field:

>  "message": "The method getCty cannot be found"

If there is an error in the operator field like:

    {
        "fieldName": "city",
        "operator": "===",
        "value": "Bari"
    }

The application returns this message because there is an incorrect operator "===":

> "message": "Illegal operator, it must be only == "

In a multiple filter with the following  incorrect body:

    {
        "and": [
            {
                "fieldName": "city",
                "operator": "==",
                "value": "Bari"
            },
            {
                "fieldName": "beginValidity",
                "operator": ">",
                "value": "01/01/2012"
            },
            {
                "fieldName": "beginValidity",
                "operator": "<",
                "value": "31/12/2012"
            }
        ]
    }
The application returns this message because there is an incorrect logical operator "and" instead of "$and":

>"message": "Incorrect JSON body"

This  message is also printed if the user writes a wrong key, for example "fieldNam" instead of "fieldName" in single or multiple filter.


 - Stats features
 
 If there is a wrong request like  

>  localhost:8080/stats/id

 the application returns a message like:

>  "String cannot be cast to class Double"

because the GET /stats/{fieldName} should only be used with numeric fields like "latitude" and "longitude". It must not be used with string fields like "id".
 


## UML

[UML CLass Diagram](https://drive.google.com/open?id=14v7iiiCaLJaf-8uphyFZHZKdVjjXm3HD)

[UML Use Case Diagram](https://drive.google.com/open?id=167rJtDSa9ACGYDunNvl6yv_7pjPrMlOI)

[UML Sequence Diagram](http://drive-html-viewer.pansy.at/?state=%7B%22ids%22:%5B%221XAUzGJLAlYnL3DmjqJb7LRBxdb-NFm9Q%22%5D,%22action%22:%22open%22,%22userId%22:%22117028957555747698312%22%7D)
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTY3NTA4OTAzNl19
-->

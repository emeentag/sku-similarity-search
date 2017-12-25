# SKU Search Problem
This application makes a similarity search according to your search object. Application needs a data file which contains several object in it like `test-data.json` file. And when you want to find similar objects according to your search objects, it reads this data source and compares each object attributes with the searched ones. Search is applied according to weight of the attributes which you can provide to the app as a env var. Please read the following to understand better.

## Technology Used
* Java 8
* Gradle
* JUnit

## Application Architecture
Application contains a webserver and according to your request this webserver creates two main thread for creating a data stream to multi threaded consumers. Lets have a look these two main stream thread. 

### Sku Generator
Sku generator is a thread and this process simply gets the json data source which is taken from the data source file you provide. In our example it is `test-data.json`. After that it starts to put each json object into a blocking queue. In the mean time this blocking queue consumes by the sku consumer manager. Whenever generator puts a new json data into queue this json data consumes by a sku consumer over sku consumer manager.

### Sku Consumer Manager
This is another main thread which runs with the generator. It takes the json objects from the blocking queue which is filled by generator. Then it assigns each object and the search object to the consumers. Sku consumers are responsibile to calculate the similarity value with our calculation logic and then they put the similarity value and the related object into a priority queue for making the search results order.

## Similarity Value Algorithm
This algorithm is used for calculating the similarity value. A consumer calculates the similarity for each attributes by comparing them with the search object. 
* For the weights you can assign different values to the attributes by changing the `WEIGHTS_MAP` in env vars. In default application takes attr-a with a bigger weight from attr-z. You can find explanation of the changing `WEIGHTS_MAP`.
* Once weight is decided, a distance needs to be found by getting a difference from the number value of the attribute. Think about an attribute like in compareObject: `"att-j":"att-j-4"` and for searchObject: `"att-j":"att-j-1"`. So the distance should be `ABS(4-1)`
* After calculating the distance next step to multiply this distance and the attribute weight.
* These steps need to be applied for the each attribute of the compare object. Then all calculated attributes results are accumulating. This accumulated result will be the similarity value of our compare object.

## How to Test
For testing the app you have to install gradle on your machine. Please note that a builded JAR file is also in repository so you do not need to build the project again. If you do not want to install gradle or others just run `./init.sh --prod` for manuel test.

### Unit Tests
Application has some unit tests. So you can run unit tests with command `gradle test --info`

### Manuel Test
For manuel testing you should run `./init.sh --prod` in a terminal session. This will run the unit tests and build a JAR file into `build/libs/recommendation_service-all-1.0.jar` and then it starts the webserver.

If you run `./init.sh` only this will initiliaze the aplication on dev mode. It is up to you which one you wanna run.

So once the web server is ready you can send a search request with the command in another terminal session `./request.sh sku-1`. This command will make a curl to the server and retrieves the results. Also you can see the results on webserver terminal output. Also according to your config a log file will be created under logs directory.

## Configurations
There are five main env configuration which you can set over `./init.sh`. Also each parameter has its own defaults.

** LOG_LEVEL: ** You can set it as `all, debug, info, off`. Default `all`.

** SERVER_PORT: ** You can set the server port. Default `8080`.

** FILE_PATH: ** Set the data source file. Default `Application will search through test-data.json file in the current directory.`

** WEIGHTS_MAP: ** You can set attributes weights like the definition: `"a:10,b:9,c:8,d:7,e:6,f:5,g:4,h:3,i:2,j:1"`. Default is like the definition. 

** NUMBER_OF_RESULTS: ** Set the number of similarity results for your search. Default is `10`.

Thanks

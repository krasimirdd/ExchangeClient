# ExchangeClient
Small exchange client application for getting data order book from Bitfinex and Kraken exchanges.

The application is Spring based, which means that you can start it using Spring Boot configuration in IDE :
<li> Main class : com.kddimitrov.exchangeClient.Application </li>
<li> Applcation configutaion file : src\main\resources\application.yml </li>

<hr>

The configuration supports properties for :
<li> Bitfinex public wss endpoint </li>
<li> Kraken public wss endpoint </li>
<li> OrderBook max size - default is 10 which means that the OrderBook will be able to store up to 10 Ask and 10 Bid entries. Upon receiving a better bid/ask the worst is replaced if the book size is reached.</li>

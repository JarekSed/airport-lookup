airport-lookup
==============

Airport Lookup Program for Distributed Systems Assignment 4

Places Server -- Jarek
    - This is mostly done. I didn't realize it had to do partial matches, so I used
        a HashMap for storing all our place data. This will have to be changed to something else (prolly a tree),
        but that shouldn't be too bad. There is a simple PlacesClient class that shows how to connect and make calls to the
        places server.

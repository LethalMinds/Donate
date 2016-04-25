# Donate
Android based charity donation application which provides different functionalities to the Donor (User) for donating money to a deserving or a needy person receiver (User).

#Installation/Download - (Make sure PATH is set)
* NodeJs - https://nodejs.org/en/download/
* MongoDB - https://www.mongodb.org/downloads#production

#Restore MongoDB
https://docs.mongodb.org/manual/tutorial/backup-and-restore-tools/

* Dump location - artifacts/mongodb/dump
* Database name - udonate
* restore command example - mongorestore --dir ~/artifacts/mongodb/dump/udonate
* Run mongoDB service, either using mongod command or as window service or as terminal batch

#Node dependencies
* Install all the node dependencies listed on package.json under Node directory
* Start node server using 'node www', 'www' file is available under directory node/bin (start nodeserver only after starting mongodb service)

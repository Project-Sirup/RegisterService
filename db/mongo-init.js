db = new Mongo().getDB("sirupReg");

db.createUser({
    user: "admin",
    pwd: "admin",
    roles: [
        {
            role: "readWrite",
            db: "sirupReg",
        },
    ],
});
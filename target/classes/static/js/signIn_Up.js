console.log("connected to sign up/in javascript");

apiJoke = () => {

    var queryUrl = "http://api.icndb.com/jokes/random/";

    $.ajax({
        url: queryUrl,
        method: "GET"
    })
    .then((res) => {
        console.log(res.value.joke);

    })
};

apiJoke();


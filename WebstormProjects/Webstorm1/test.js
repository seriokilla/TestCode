var http = require('http'),
    fs = require('fs');

var inputfilepath = 'C:/jlai/aac/01-Rock/';
fs.readdir(inputfilepath, function(err, files){
    if (err)
        console.log(err)
    else {
        songs = files;
    }
});


function randomInt (low, high) {
    var randnum = Math.floor(Math.random() * (high - low) + low);
    return randnum;
}

http.createServer(doresponse).listen(3001);

function doresponse(request, response) {
    var idx = randomInt(0, songs.length-1);
    var song = songs[idx];
    console.log(song);
    response.end('<audio controls="controls" loop="loop"><source src="http://westciv.com/podcasts/youmayknowmypoetry.mp3"></audio>');
}
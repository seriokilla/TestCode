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

http.createServer(function(request, response) {
    var idx = randomInt(0, songs.length - 1);
    var filePath = inputfilepath + songs[idx];
    console.log('picking: ' + filePath + ' ' + idx);

    var stat = fs.statSync(filePath);
    response.writeHead(200, {
        'Content-Type': 'audio/mpeg',
        'Content-Length': stat.size
    });

    var readStream = fs.createReadStream(filePath);

    readStream.on('data', function(data) {
        response.write(data);
    });

    readStream.on('end', function() {
    });
}).listen(3000);


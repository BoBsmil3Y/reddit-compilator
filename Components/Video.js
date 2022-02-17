const Media = require("./Media.js");

module.exports = class Video extends Media {

    author;
    duration;

    constructor(url, downloadLink, author, duration){
        super(url, downloadLink);
        this.author = author;
        this.duration = duration;
    }

}
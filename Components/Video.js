const Media = require("./Media.js");

module.exports = class Video extends Media {

    author;
    duration;

    constructor(url, permalink, author, duration){
        super(url, permalink);
        this.author = author;
        this.duration = duration;
    }

}
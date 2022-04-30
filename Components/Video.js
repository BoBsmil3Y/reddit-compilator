const Media = require("./Media.js");

module.exports = class Video extends Media {

    author;
    duration;
    title;

    constructor(url, permalink, author, duration, title){
        super(url, permalink);
        this.author = author;
        this.duration = duration;
        this.title = title;
    }

}
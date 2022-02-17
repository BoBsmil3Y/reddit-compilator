const Media = require("./Media.js");

module.exports = class Video extends Media {

    author;

    constructor(url, author){
        super(url);
        this.author = author;
    }

}
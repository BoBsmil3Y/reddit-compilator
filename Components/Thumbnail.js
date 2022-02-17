const Media = require("./Media.js");

module.exports = class Thumbnail extends Media {

    name;

    constructor(url, name){
        super(url);
        this.name = name;
    }

}
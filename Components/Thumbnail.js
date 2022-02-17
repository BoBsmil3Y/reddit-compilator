const Media = require("./Media.js");

module.exports = class Thumbnail extends Media {

    constructor(url, downloadLink){
        super(url, downloadLink);
    }

}
module.exports = class Subreddit {

    name;
    pourcentage;
    minDuration;
    maxDuration;

    constructor(name, pourcentage, minDuration, maxDuration){
        this.name = name;
        this.pourcentage = pourcentage;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
    }

}
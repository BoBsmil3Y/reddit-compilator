const Thumbnail = require("../Components/Thumbnail");

module.exports = class Controller {
    
    static today = new Date();
    static yesterday = new Date().setDate(this.today.getDate() -1);
    
    /**
     * Entry: arraylist of reddit post (images)
     * Exit: a Thumbnail object with the most liked image 
     */
    static chooseThumbnail(thumbnails){
        
        let url = "", score = 0, name, regex = /(.*?)cursed_\S+/;

        for(let image of thumbnails.children){
            const data = image.data;

            // If it's more than one day old
            if(! data.created*1000 > this.yesterday) break;

            // Take the image with the best score
            if(! data.over_18 && data.score > score && ! data.is_video && ! data.over_18 && ! data.title.includes("Removed by reddit")){
                score = data.score;
                url = data.permalink;
                name = data.permalink.replace(regex, "$1");
            }
        }
        
        if(url === "") throw "No thumbnail found for today";

        return new Thumbnail(url, name);
    }
}
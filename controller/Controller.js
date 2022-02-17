const Thumbnail = require("../Components/Thumbnail");

module.exports = class Controller {
    
    static today = new Date();
    static yesterday = new Date().setDate(this.today.getDate() -1);
    
    /**
     * Entry: arraylist of reddit post (images)
     * Exit: a Thumbnail object with the most liked image 
     */
    static chooseThumbnail(thumbnails){
        
        let downloadLink = "", url, score = 0;

        for(let image of thumbnails.children){
            const data = image.data;

            // If it's more than one day old
            if(! data.created*1000 > this.yesterday) break;

            // Take the image with the best score
            if(! data.over_18 && data.score > score && ! data.is_video && ! data.title.includes("Removed by reddit") && ! data.pinned){
                score = data.score;

                downloadLink = data.url;
                url = data.permalink;
            }
        }
        
        if(downloadLink === "") throw "No thumbnail found for today";

        return new Thumbnail(url, downloadLink);
    }
}

//name = data.permalink.replace(regex, '').replace('/', '');
//regex = /(\/r\/Cursed_Images\/comments\/)([A-Za-z]*\/)/
const Subreddit = require("../Components/Subreddit");
const Thumbnail = require("../Components/Thumbnail");
const Video = require("../Components/Video");

const today = new Date();
const yesterday = new Date().setDate(today.getDate() -1);

module.exports = class Controller {
    
    /**
     * Will return the image with the highest score from the 
     * given list. It will not take image that are NSWF, a video,
     * pinned, or has been removed.
     * @param  {Array<String>} posts A list of Reddit posts
     * @return {Thumbnail}           The best image from the list
     */
    static chooseThumbnail(posts){
        let permalink = "", url, score = 0;

        for(let image of posts.children){
            const data = image.data;
            // If it's more than one day old
            if(! isFromToday(data.created)) break;
            // Take the image with the best score
            if(! data.over_18 && data.score > score && ! data.is_video && ! data.title.includes("Removed by reddit") && ! data.pinned){
                score = data.score;
                url = data.url;
                permalink = data.permalink;
            }
        }

        if(permalink === "") throw "No thumbnail found for today";

        return new Thumbnail(url, permalink);
    }

    /**
     * Will return an array of videos with the total video time wanted.
     * They are gonna be the greatest videos of the selection.
     * @param  {Subreddit} subreddit The subreddit information needed
     * @param  {Array<String>} posts A list of Reddit posts
     * @return {Array<Video>}        A list of the best videos from the list
     */
    static chooseVideos(subreddit, posts, videoLength){
        let sum = 0, filtered = [], selected = [];

        filtered = filterVideos(subreddit, posts);

        filtered.sort((a, b) => a.data.score - b.data.score);

        for(let i = filtered.length-1; i >= 0; i--){
            if(sum >= (subreddit.pourcentage/100)*videoLength) break;

            sum += filtered[i].data.media.reddit_video.duration;
            selected.push(new Video(filtered[i].data.media.reddit_video.fallback_url));
        }
        console.log("Subreddit : " + subreddit.name + " Durée totale : " + sum + ", soit " + (sum/videoLength)*100 + "% sur " + subreddit.pourcentage + "% demandés");

        return selected;
    }

}

function isFromToday(theDate){
    return (theDate*1000) > yesterday;
}

function filterVideos(subreddit, posts){
    let selected = [];

    posts.forEach(temp => {
        const post = temp.data;
        
        if(post.is_video && 
            ! post.over_18 && 
            ! isFromToday(post.created) &&
            post.media.reddit_video.duration >= subreddit.minDuration  && 
            post.media.reddit_video.duration <= subreddit.maxDuration && 
            ! post.media.reddit_video.is_gif){
                selected.push(temp);
        } // post.media.reddit_video.height >= 720 && 
    });

    return selected;
}

//name = data.permalink.replace(regex, '').replace('/', '');
//regex = /(\/r\/Cursed_Images\/comments\/)([A-Za-z]*\/)/
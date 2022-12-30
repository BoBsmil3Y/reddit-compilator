const Subreddit = require("../Components/Subreddit");
const Thumbnail = require("../Components/Thumbnail");
const Video = require("../Components/Video");
const { exec } = require('child_process');


const today = new Date();
const yesterday = new Date().setDate(today.getDate() -1);

module.exports = class MediaController {

    /**
     * Will return 3 images with the highest score from the
     * given list. It will not take image that are NSWF, a video,
     * pinned, or has been removed by mods.
     * @param  {Array<String>} posts A list of Reddit posts
     * @return {Array<Thumbnail>} The three best images from the list
     */
    static chooseThumbnail(posts){
        let thumbnails = [], post;

        posts.children.sort((a, b) => b.data.score - a.data.score);

        while(thumbnails.length < 3 && posts.children.length > 0){

            post = posts.children.shift();
            post = post.data;

            if(! isFromToday(post.created) || post.over_18 || post.is_video
                || post.title.includes("Removed by reddit") || post.pinned)
                continue;

            thumbnails.push(new Thumbnail(post.url, post.permalink));
        }

        if(thumbnails.length == 0) throw "No thumbnail found for today";

        return thumbnails;
    }

    /**
     * Will return an array of videos with the total subreddit video time wanted.
     * Sort by score, and filter with many conditions.
     * @param  {Subreddit}     subreddit   The subreddit information needed
     * @param  {Array<String>} posts       A list of Reddit posts to work with
     * @param  {Number}        videoLength The length wanted for the compilation
     * @return {Array<Video>}              A list of the best videos from the list
     */
    static chooseVideos(subreddit, posts, videoLength){
        let sum = 0, post, selected = [];
        const pourcentage = (subreddit.pourcentage/100)*videoLength;

        const filtered = posts.sort((a, b) => b.data.score - a.data.score);

        while(sum <= pourcentage && filtered.length > 0){

            post = filtered.shift();
            post = post.data;

            if(! post.is_video || post.over_18 || ! isFromToday(post.created) ||
                post.media.reddit_video.duration <= subreddit.minDuration  ||
                post.media.reddit_video.duration >= subreddit.maxDuration ||
                post.media.reddit_video.is_gif || post.title.includes("Removed by reddit")
                || post.pinned)
                continue;

            sum += post.media.reddit_video.duration;
            selected.push(new Video(post.media.reddit_video.fallback_url));
        }

        console.log(subreddit.name + " : (durée totale) " + sum + "s, soit " + (sum/videoLength)*100 + "% sur " + subreddit.pourcentage + "% demandés");
        return selected;
    }

    static merge(number){

        const dwlPath = `Downloads/${number}`

        exec(`ffmpeg -i ${dwlPath}-video.mp4 -i ${dwlPath}-audio.mp4 -c:v copy -c:a aac -map 0:v:0 -map 1:a:0 ${dwlPath}-FINAL.mp4`, (error) => {
            if (error) {
                //console.error(`${number} -> Error when merging video and audio, code : ${error.code}`);
                console.error(error.message);
                return;
            }

            console.log(`${number} -> Merge succefully.`);
        });
    }

}

/**
 * @param {Date} theDate An epoch date
 * @returns <code>true</code> if the date is from today
 */
function isFromToday(theDate){
    return (theDate*1000) > yesterday;
}
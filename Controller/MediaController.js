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

            if(! isFromToday(post.created) && ! post.over_18 && ! post.is_video && ! post.title.includes("Removed by reddit") && ! post.pinned)
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
        let sum = 0, filtered = [], selected = [];
        const pourcentage = (subreddit.pourcentage/100)*videoLength;

        filtered = filterVideos(subreddit, posts);
        filtered.sort((a, b) => a.data.score - b.data.score);

        //TESTER SI C'EST BIEN TRIE
        for (let i = 0; i < posts.children.length; i++)
            console.log(posts.children[i].data.score)

        for(let i = filtered.length-1; i >= 0; i--){
            if(sum >= pourcentage) break;

            sum += filtered[i].data.media.reddit_video.duration;
            selected.push(new Video(filtered[i].data.media.reddit_video.fallback_url));
        }
        console.log("Subreddit : " + subreddit.name + " Durée totale : " + sum + ", soit " + (sum/videoLength)*100 + "% sur " + subreddit.pourcentage + "% demandés");

        return selected;
    }

    static mergeAudioAndVideo(count){
        let dwlPath;

        for(let i = 1; i < count; i++){
            dwlPath = `Downloads/${i}`

            exec(`ffmpeg -i ${dwlPath}-video.mp4 -i ${dwlPath}-audio.mp4 -c copy -c:v libx264 -c:a aac ${dwlPath}-FINAL.mp4`, (error) => {
                if (error) {
                    console.error(`${i} video : Error when merging video and audio, code : ${error.code}`);
                    console.error(error.message);
                    return;
                }
                console.log(`${i} video : Merge succefully.`);
                //TODO Gérer l'erreur si on a un fichier invalide -> lancer la suppression que pour la vidéo
                /*
                exec(`rm ${dwlPath}-video.mp4 ${dwlPath}-audio.mp4`, (error) => {
                    if (error) {
                        console.error(`exec error: ${error}`);
                        return;
                    }
                });
                */
            });
        }
    }

}

/**
 * @param {Date} theDate An epoch date
 * @returns <code>true</code> if the date is from today
 */
function isFromToday(theDate){
    return (theDate*1000) > yesterday;
}

/**
 * Will return all the valid videos from the given array of posts.
 * @param   {Array<Subreddit>} subreddit The subreddit object to take infos from
 * @param   {Array<Object>}    posts     The array of posts to check
 * @returns {Array<Object>}              The array of posts that are valid
 */
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
        }
    });

    return selected;
}
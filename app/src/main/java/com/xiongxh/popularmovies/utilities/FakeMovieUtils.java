package com.xiongxh.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.renderscript.Double2;

import com.xiongxh.popularmovies.data.MovieContract;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.action;
import static android.R.attr.id;
import static android.R.attr.order;

public class FakeMovieUtils {

    private static String[][] movieData = new String[][]{
            {"/tWqifoYuwLETmmasnGHO7xBjEtt.jpg",
             "A live-action adaptation of Disney's version of the classic 'Beauty and the Beast' tale of a cursed prince and a beautiful young woman who helps him break the spell.",
            "2017-03-16",
            "321612",
            "Beauty and the Beast",
            "en",
            "/6aUWe0GSl69wMTSWWexsorMIvwU.jpg",
            "136.40833",
            "1883",
            "6.9"
            },
            {"/unPB1iyEeTBcKiLg8W083rlViFH.jpg",
            "A story about how a new baby's arrival impacts a family, told from the point of view of a delightfully unreliable narrator, a wildly imaginative 7 year old named Tim.",
            "2017-03-23",
            "295693",
            "The Boss Baby",
            "en",
            "/bTFeSwh07oX99ofpDI4O2WkiFJ.jpg",
            "131.548998",
            "643",
            "5.7"
            },
            {"/iNpz2DgTsTMPaDRZq2tnbqjL2vF.jpg",
            "When a mysterious woman seduces Dom into the world of crime and a betrayal of those closest to him, the crew face trials that will test them as never before.",
            "2017-04-12",
            "337339",
            "The Fate of the Furious",
            "en",
            "/jzdnhRhG0dsuYorwvSqPqqnM1cV.jpg",
            "89.434021",
            "788",
            "7"
            },
            {"/45Y1G5FEgttPAwjTYic6czC9xCn.jpg",
            "In the near future, a weary Logan cares for an ailing Professor X in a hide out on the Mexican border. But Logan's attempts to hide from the world and his legacy are up-ended when a young mutant arrives, being pursued by dark forces.",
            "2017-02-28",
            "263115",
            "Logan",
            "en",
            "/5pAGnkFYSsFJ99ZxDIYnhQbQFXs.jpg",
            "63.384449",
            "2582",
            "7.5"
            },
            {"/eSVtBB2PVFbQiFWC7CQi3EjIZnn.jpg",
            "A koala named Buster recruits his best friend to help him drum up business for his theater by hosting a singing competition.",
            "2016-11-23",
            "335797",
            "Sing",
            "en",
            "/wf9C6iqY9tXG7TrQDg3E4Flgap7.jpg",
            "61.190273",
            "1236",
            "6.7"
            },
            {"/myRzRzCxdfUWjkJWgpHHZ1oGkJd.jpg",
            "In the near future, Major is the first of her kind: a human saved from a terrible crash, who is cyber-enhanced to be a perfect soldier devoted to stopping the world's most dangerous criminals.",
            "2017-03-29",
            "315837",
            "Ghost in the Shell",
            "en",
            "/lsRhmB7m36pEX0UHpkpJSE48BW5.jpg",
            "46.632306",
            "676",
            "6"
            },
            {"/r2517Vz9EhDhj88qwbDVj8DCRZN.jpg",
            "Explore the mysterious and dangerous home of the king of the apes as a team of explorers ventures deep inside the treacherous, primordial island.",
            "2017-03-08",
            "293167",
            "Kong: Skull Island",
            "en",
            "/pGwChWiAY1bdoxL79sXmaFBlYJH.jpg",
            "41.742252",
            "1303",
            "6"
            },
            {"/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg",
            "Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.",
            "2015-06-09",
            "135397",
            "Jurassic World",
            "en",
            "/dkMD5qlogeRMiEixC4YNPUvax2T.jpg",
            "37.649877",
            "6725",
            "6.5"
            },
            {"/rXMWOZiCt6eMX22jWuTOSdQ98bY.jpg",
            "Though Kevin has evidenced 23 personalities to his trusted psychiatrist, Dr. Fletcher, there remains one still submerged who is set to materialize and dominate all the others. Compelled to abduct three teenage girls led by the willful, observant Casey, Kevin reaches a war for survival among all of those contained within him — as well as everyone around him — as the walls between his compartments shatter apart.",
            "2016-11-15",
            "381288",
            "Split",
            "en",
            "/4G6FNNLSIVrwSRZyFs91hQ3lZtD.jpg",
            "35.245127",
            "1918",
            "6.8"
            },
            {"/y31QB9kn3XSudA15tV7UWQ9XLuW.jpg",
            "Light years from Earth, 26 years after being abducted, Peter Quill finds himself the prime target of a manhunt after discovering an orb wanted by Ronan the Accuser.",
            "2014-07-30",
            "118340",
            "Guardians of the Galaxy",
            "en",
            "/bHarw8xrmQeqf3t8HpuMY7zoK4x.jpg",
            "34.632274",
            "6888",
            "7.9"}
            };

    private static ContentValues createTestMoveContentValues(String[] str){
        ContentValues testContenValues = new ContentValues();
        testContenValues.put(MovieContract.MovieEntry.COLUMN_POSTER, str[0]);
        testContenValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,str[1]);
        testContenValues.put(MovieContract.MovieEntry.COLUMN_DATE, str[2]);
        testContenValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, Long.valueOf(str[3]));
        testContenValues.put(MovieContract.MovieEntry.COLUMN_TITLE, str[4]);
        testContenValues.put(MovieContract.MovieEntry.COLUMN_LANGUAGE, str[5]);
        testContenValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP, str[6]);
        testContenValues.put(MovieContract.MovieEntry.COLUMN_POP, Double.valueOf(str[7]));
        testContenValues.put(MovieContract.MovieEntry.COLUMN_VOTENUM, Integer.valueOf(str[8]));
        testContenValues.put(MovieContract.MovieEntry.COLUMN_VOTESCORE, Double.valueOf(str[9]));

        return testContenValues;
    }

    public static void insertFakeData(Context context){

        List<ContentValues> fakeValues = new ArrayList<ContentValues>();

        for(String[] s : movieData){
            fakeValues.add(createTestMoveContentValues(s));
        }

        context.getContentResolver().bulkInsert(
                MovieContract.MovieEntry.CONTENT_URI,
                fakeValues.toArray(new ContentValues[10]));
    }

}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.Track;

public class SpotifyCore
{

	public static void main(String[] args)
	{
		final String clientId = "";
		final String clientSecret = "";
		final String playlistID = "";
		final String me = "";

		final Api api = Api.builder().clientId(clientId).clientSecret(clientSecret).build();

		/* Create a request object. */
		final ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();

		/* Use the request object to make the request, either asynchronously (getAsync) or synchronously (get) */
		final SettableFuture<ClientCredentials> responseFuture = request.getAsync();

		/* Add callbacks to handle success and failure */
		Futures.addCallback(responseFuture, new FutureCallback<ClientCredentials>()
		{
			@Override
			public void onSuccess(ClientCredentials clientCredentials)
			{
				/* The tokens were retrieved successfully! */
				System.out.println("Successfully retrieved an access token! " + clientCredentials.getAccessToken());
				System.out.println("The access token expires in " + clientCredentials.getExpiresIn() + " seconds");

				/* Set access token on the Api object so that it's used going forward */
				api.setAccessToken(clientCredentials.getAccessToken());

				try
				{
					final Playlist playlist = api.getPlaylist(me, playlistID).build().get();

					System.out.println("Retrieved playlist " + playlist.getName());
					System.out.println(playlist.getDescription());
					System.out.println("It contains " + playlist.getTracks().getTotal() + " tracks");

					List<String> tracks = new ArrayList<String>();

					File f = new File("res/Top5Songs.txt");
					BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
					String line = "";
					while((line = reader.readLine()) != null)
					{
						final Page<Track> trackSearchResult = api.searchTracks(line).market("US").build().get();
						if(trackSearchResult.getTotal() == 0)
						{
							System.out.println(line + " could not be found!");
						}
						else
						{
							Track track = trackSearchResult.getItems().get(0);
							System.out.println("Song obtained: " + track.getName());
							if(!playListHasTrack(playlist, track.getId()))
								tracks.add(track.getUri());
							else
								System.out.println("Song: " + track.getName() + " is a duplicate!");
						}
						Thread.sleep(1000);
					}
					api.addTracksToPlaylist(me, playlistID, tracks);
					StringBuilder builder = new StringBuilder();
					for(String song : tracks)
						builder.append(song + "\n");

					BufferedWriter bw = null;
					FileWriter fw = null;

					try
					{
						fw = new FileWriter("songURIs.txt");
						bw = new BufferedWriter(fw);
						bw.write(builder.toString());
					} catch(IOException e)
					{
						e.printStackTrace();
					} finally
					{
						try
						{
							if(bw != null)
								bw.close();

							if(fw != null)
								fw.close();
						} catch(IOException ex)
						{
							ex.printStackTrace();
						}
					}
					System.out.println("Tracks added!");
					reader.close();

				} catch(Exception e)
				{
					System.out.println("Something went wrong!" + e.getMessage());
				}

			}

			@Override
			public void onFailure(Throwable throwable)
			{
				throwable.printStackTrace();
			}
		});
	}

	public static boolean playListHasTrack(Playlist playlist, String id)
	{
		for(PlaylistTrack track : playlist.getTracks().getItems())
			if(track.getTrack().getId().equals(id))
				return true;

		return false;
	}
}

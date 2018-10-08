package io.hamo.qdio.room;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.Queue;

import io.hamo.qdio.MusicData.MusicDataServiceFactory;
import io.hamo.qdio.SongHistory;
import io.hamo.qdio.SongQueueList;
import io.hamo.qdio.communication.Communicator;
import io.hamo.qdio.communication.JsonUtil;
import io.hamo.qdio.communication.entity.CommandAction;
import io.hamo.qdio.communication.entity.CommandMessage;
import io.hamo.qdio.music.Track;

public class SlaveRoom implements Room {

    private final Communicator communicator;
    private final RoomData roomData;
    private static final RoomType type = RoomType.SLAVE;

    public SlaveRoom(Communicator com){
        roomData = new RoomData();
        this.communicator = com;
        com.getIncomingMessages().observeForever(new Observer<Queue<CommandMessage>>() {
            @Override
            public void onChanged(@Nullable Queue<CommandMessage> commandMessages) {
                while(!commandMessages.isEmpty()){
                    CommandMessage cmdMsg = commandMessages.poll();
                    switch (cmdMsg.getAction()){
                        case NOTIFY_UPDATE:
                            Log.i(getClass().getSimpleName(), cmdMsg.getValue());
                            handleNotifyUpdate(cmdMsg.getValue());
                            break;
                        case ADD_SONG:
                            break;
                            default:
                                Log.w(getClass().getSimpleName(), "Bad command message");
                                break;
                    }
                }
            }
        });
    }

    private void handleNotifyUpdate(String msg){
        SerializableRoom sRoom = JsonUtil.getInstance().deserializeRoom(msg);
        try {
            roomData.setCurrentTrack(MusicDataServiceFactory
                    .getService()
                    .getTrackFromUri(sRoom.getCurrentTrackURI())
                    .call());
            for (String s : sRoom.getQueueList()){
                roomData.getQueueList().addSong(MusicDataServiceFactory
                        .getService()
                        .getTrackFromUri(s)
                        .call());
            }
            for (String s : sRoom.getHistoryList()){
                roomData.getHistory().add(MusicDataServiceFactory
                        .getService()
                        .getTrackFromUri(s)
                        .call());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addToQueue(Track track) {
        communicator.sendCommand(
                new CommandMessage(CommandAction.ADD_SONG, track.getURI())
        );
    }

    @Override
    public SongQueueList getQueueList() {
        return roomData.getQueueList();
    }

    @Override
    public SongHistory getHistory() {
        return roomData.getHistory();
    }

    @Override
    public Track getCurrentSong() {
        return roomData.getCurrentTrack();
    }

    @Override
    public RoomType getType() {
        return type;
    }
}

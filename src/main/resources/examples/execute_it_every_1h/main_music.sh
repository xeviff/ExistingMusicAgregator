echo "> Hi. Very welcome to music agregator. This is $(date +"%Y_%m_%d")"
echo "> starting Java app and save the output to log file..."
cd "/your_device/java projects/ExistingMusicAgregator"
echo "> docker-compose down and up"
docker-compose down
echo "> down complete, now up"
docker-compose up > ~/logs/java/music/$(date +"%Y-%m-%d_%H-%M")_artist_move.log
echo "> "
echo "> done!"
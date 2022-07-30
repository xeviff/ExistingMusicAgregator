echo "> Hi. Very welcome to music agregator. This is $(date +"%Y_%m_%d")"
echo "> starting Java app and save the output to log file..."
cd "/your_device/java projects/ExistingMusicAgregator"
echo "> docker-compose down and up"
docker-compose down
echo "> down complete, now up"
filename=$(date +"%Y-%m-%d_%H-%M")_artist_move.log
docker-compose up > ~/logs/java/music/$filename
echo "$(cat ~/logs/java/music/$filename)"
echo "> "
echo "> done!"
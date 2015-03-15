texttalk-backend
================

#Text to Speech processing module

##To launch VM
https://docs.google.com/document/d/1cHSBVwYDZZmSPqS3de_9SrR-qstLxPYucRkIqZETqNk/edit?usp=sharing

##To run speech synthesis via command line
echo "message" | /vagrant/apps/psola_transcriber/transcribe 32768 0 0 - - | /vagrant/apps/psola_synthesizer/sint_psola /vagrant/apps/psola_synthesizer/PSOLADB.DAT 1100 - - | /usr/bin/sox -v 2.0 -t raw -r 22050 -b 16 -s -c 1 - -t wav - |/usr/bin/lame -f --resample 22 -b 32 - /vagrant/storage/voice/output.mp3

or

echo "message" | /vagrant/apps/psola_transcriber/transcribe 32768 0 0 - - | /vagrant/apps/psola_synthesizer/sint_psola /vagrant/apps/psola_synthesizer/PSOLADB.DAT 1100 - - | /vagrant/apps/lame_encoder/lame > /vagrant/storage/voice/output.mp3

##Commands

###To submit Storm topology
storm jar com.texttalk.backend-jar-with-dependencies.jar com.texttalk.distrib.storm.TextToSpeechTopology text-to-speech-topology

###To submit to remote Storm topology

storm jar com.texttalk.backend-jar-with-dependencies.jar com.texttalk.distrib.storm.TextToSpeechTopology text-to-speech-topology -c nimbus.host=xeon.host.tele1.co -c nimbus.thrift.port=49627

###View Storm worker log
tail -f /usr/share/storm/logs/worker-6705.log -n 1000

###To login to Redis CLI
redis-cli -h s01.tele1.co -p 56379 -a password

###Add text to be synthesized to Redis queue via CLI
RPUSH tts-in '{"id": "text1", "channel": "c1", "orderId": 0, "hashCode": "abcdefgh123456", "text": "message", "synth": "luss", "voice": "Regina"}'



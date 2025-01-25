Tower {
  var <params;
  var <loopers;

  *initClass {
	StartUp.add {
	  var s = Server.default;

	  s.waitForBoot {
		SynthDef("MonoLooper", {
		  arg out = 0,
		  bufnum = 0,
		  rate = 1,
		  amp = 1,
		  lpfreq = 20000,
		  hpfreq = 20,
		  pos = 0;

		  var snd, frames;

		  rate = rate*BufRateScale.kr(bufnum);

		  frames = BufFrames.kr(bufnum);

		  snd = PlayBuf.ar(
			numChannels:1,
			bufnum:bufnum,
			rate: rate,
			startPos: 0,
			loop: 1
		  );

		  snd = HPF.ar(in: snd, freq: hpfreq);
		  snd = LPF.ar(in: snd, freq: lpfreq);

		  snd = snd * amp;
		  snd = Pan2.ar(snd, pos: pos);

		  Out.ar(out, snd);
		}).add;

		SynthDef("StereoLooper", {
		  arg out = 0,
		  bufnum = 0,
		  rate = 1,
		  amp = 1,
		  lpfreq = 20000,
		  hpfreq = 20,
		  pos = 0;

		  var snd, frames;

		  rate = rate*BufRateScale.kr(bufnum);

		  frames = BufFrames.kr(bufnum);

		  snd = PlayBuf.ar(
			numChannels:2,
			bufnum:bufnum,
			rate: rate,
			startPos: 0,
			loop: 1
		  );

		  snd = HPF.ar(in: snd, freq: hpfreq);
		  snd = LPF.ar(in: snd, freq: lpfreq);

		  snd = snd * amp;
		  snd = Balance2.ar(snd[0], snd[1], pos: pos);

		  Out.ar(out, snd);
		}).add;
	  }
	}
  }

  *new {
	^super.new.init;
  }

  init {
	params = Dictionary.newFrom([
	  \rate, 1,
	  \amp, 1,
	  \lpfreq, 20000,
	  \hpfreq, 20,
	  \pos, 0
	]);

	loopers = Array.newClear(indexedSize: 16);
  }

  playMonoLoop { arg bufnum, index;
	var s = Synth.new("MonoLooper", [\bufnum, bufnum] ++ params.getPairs);
	loopers.put(index, s);
  }

  playStereoLoop { arg bufnum, index;
	var s = Synth.new("StereoLooper", [\bufnum, bufnum] ++ params.getPairs);
	loopers.put(index, s);
  }

  loadLoop { arg fn, index;
	Buffer.read(Server.default, fn, action:
	  { arg buffer;
		if( buffer.numChannels == 1) {
		  this.playMonoLoop(buffer.bufnum, index);
		} {
		  this.playStereoLoop(buffer.bufnum, index);
		};
	  }
	);
  }

  setParam { arg paramKey, paramValue, index;
	var looper = loopers[index];
	looper.set(paramKey, paramValue);
  }
}
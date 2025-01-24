Tower {
  var <params;

  *initClass {
	StartUp.add {
	  var s = Server.default;

	  s.waitForBoot {
		SynthDef("MonoLooper", {
		  arg out = 0,
		  bufnum = 0,
		  rate = 1;

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
	  \rate, 1;
	]);
  }

  playLoop { arg bufnum;
	Synth.new("MonoLooper", [\bufnum, bufnum] ++ params.getPairs);
  }

  loadLoop { arg fn;
	Buffer.read(Server.default, fn, action: this.playLoop);
  }

  setParam { arg paramKey, paramValue;
	params[paramKey] = paramValue;
  }
}
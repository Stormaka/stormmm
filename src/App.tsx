/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import React, { useState, useRef, useEffect } from 'react';
import { Play, Pause, SkipBack, SkipForward, Volume2 } from 'lucide-react';
import { motion, AnimatePresence } from 'motion/react';

export default function App() {
  const [isPlaying, setIsPlaying] = useState(false);
  const [progress, setProgress] = useState(0);
  const [duration, setDuration] = useState(0);
  const [currentTime, setCurrentTime] = useState(0);
  
  const audioRef = useRef<HTMLAudioElement>(null);
  const progressBarRef = useRef<HTMLInputElement>(null);

  const audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
  const imageUrl = "https://images.unsplash.com/photo-1614613535308-eb5fbd3d2c17?q=80&w=1000&auto=format&fit=crop";

  useEffect(() => {
    const audio = audioRef.current;
    if (!audio) return;

    const updateProgress = () => {
      setCurrentTime(audio.currentTime);
      setProgress((audio.currentTime / audio.duration) * 100 || 0);
    };

    const onLoadedMetadata = () => {
      setDuration(audio.duration);
    };

    const onEnded = () => {
      setIsPlaying(false);
      setProgress(0);
      setCurrentTime(0);
    };

    audio.addEventListener('timeupdate', updateProgress);
    audio.addEventListener('loadedmetadata', onLoadedMetadata);
    audio.addEventListener('ended', onEnded);

    return () => {
      audio.removeEventListener('timeupdate', updateProgress);
      audio.removeEventListener('loadedmetadata', onLoadedMetadata);
      audio.removeEventListener('ended', onEnded);
    };
  }, []);

  const togglePlay = () => {
    if (audioRef.current) {
      if (isPlaying) {
        audioRef.current.pause();
      } else {
        audioRef.current.play();
      }
      setIsPlaying(!isPlaying);
    }
  };

  const handleSeek = (e: React.ChangeEvent<HTMLInputElement>) => {
    const seekTime = (Number(e.target.value) / 100) * duration;
    if (audioRef.current) {
      audioRef.current.currentTime = seekTime;
      setProgress(Number(e.target.value));
    }
  };

  const formatTime = (time: number) => {
    const minutes = Math.floor(time / 60);
    const seconds = Math.floor(time % 60);
    return `${minutes}:${seconds.toString().padStart(2, '0')}`;
  };

  return (
    <div className="min-h-screen bg-[#0a0a0a] text-white flex flex-col items-center justify-center p-6 font-sans">
      <audio ref={audioRef} src={audioUrl} />
      
      <motion.div 
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="w-full max-w-md bg-[#151515] rounded-[2rem] p-8 shadow-2xl border border-white/5"
      >
        {/* Album Art */}
        <div className="relative aspect-square w-full mb-8 overflow-hidden rounded-2xl shadow-lg group">
          <motion.img
            src={imageUrl}
            alt="Album Art"
            className="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110"
            referrerPolicy="no-referrer"
            animate={{ scale: isPlaying ? 1.05 : 1 }}
            transition={{ duration: 2, repeat: isPlaying ? Infinity : 0, repeatType: "reverse" }}
          />
          <div className="absolute inset-0 bg-gradient-to-t from-black/40 to-transparent" />
        </div>

        {/* Track Info */}
        <div className="mb-8 text-center">
          <h2 className="text-2xl font-bold tracking-tight mb-1">Midnight City</h2>
          <p className="text-white/50 text-sm font-medium uppercase tracking-widest">SoundHelix Artist</p>
        </div>

        {/* Progress Bar */}
        <div className="mb-8">
          <div className="flex justify-between text-[10px] font-mono text-white/40 mb-2 uppercase tracking-tighter">
            <span>{formatTime(currentTime)}</span>
            <span>{formatTime(duration)}</span>
          </div>
          <input
            type="range"
            ref={progressBarRef}
            value={progress}
            onChange={handleSeek}
            className="w-full h-1 bg-white/10 rounded-full appearance-none cursor-pointer accent-white hover:accent-emerald-400 transition-all"
            style={{
              background: `linear-gradient(to right, white ${progress}%, rgba(255,255,255,0.1) ${progress}%)`
            }}
          />
        </div>

        {/* Controls */}
        <div className="flex items-center justify-between px-4">
          <button className="text-white/40 hover:text-white transition-colors">
            <SkipBack size={24} />
          </button>
          
          <motion.button
            whileTap={{ scale: 0.9 }}
            onClick={togglePlay}
            className="w-16 h-16 bg-white text-black rounded-full flex items-center justify-center shadow-xl hover:bg-emerald-400 transition-colors"
          >
            {isPlaying ? <Pause size={28} fill="currentColor" /> : <Play size={28} className="ml-1" fill="currentColor" />}
          </motion.button>

          <button className="text-white/40 hover:text-white transition-colors">
            <SkipForward size={24} />
          </button>
        </div>

        {/* Volume Indicator (Visual only for now) */}
        <div className="mt-8 flex items-center justify-center gap-3 text-white/20">
          <Volume2 size={14} />
          <div className="w-24 h-0.5 bg-white/5 rounded-full overflow-hidden">
            <div className="w-2/3 h-full bg-white/20" />
          </div>
        </div>
      </motion.div>

      {/* Background Atmosphere */}
      <div className="fixed inset-0 -z-10 overflow-hidden pointer-events-none">
        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[800px] h-[800px] bg-emerald-500/10 rounded-full blur-[120px] opacity-50" />
        <div className="absolute top-0 right-0 w-[400px] h-[400px] bg-blue-500/5 rounded-full blur-[100px]" />
      </div>
    </div>
  );
}

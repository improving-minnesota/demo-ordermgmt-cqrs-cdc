"use client";

import Image from "next/image";
import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";

// Screen components
const PromotionScreen = ({ onNext }: { onNext: () => void }) => (
  <motion.div
    className="card"
    initial={{ opacity: 0, x: 300 }}
    animate={{ opacity: 1, x: 0 }}
    exit={{ opacity: 0, x: -300 }}
    transition={{ duration: 0.5 }}
  >
    <h1 className="card-title">Promotion</h1>
    <div className="card-image-container">
      <Image
        src="/imgs/item-snickers.svg"
        alt="Snickers"
        width={250}
        height={250}
        className="card-image"
      />
    </div>
    <button
      onClick={onNext}
      className="card-button"
    >
      WISH I had a Snickers
    </button>
  </motion.div>
);

const LocationScreen = ({ onNext }: { onNext: () => void }) => (
  <motion.div
    className="card"
    initial={{ opacity: 0, x: 300 }}
    animate={{ opacity: 1, x: 0 }}
    exit={{ opacity: 0, x: -300 }}
    transition={{ duration: 0.5 }}
  >
    <h1 className="card-title">Where are You?</h1>
    <div className="card-image-container">
      <Image
        src="/imgs/location.png"
        alt="Location"
        width={250}
        height={250}
        className="card-image"
      />
    </div>
    <button
      onClick={onNext}
      className="card-button"
    >
      HERE I am
    </button>
  </motion.div>
);

const PaymentScreen = ({ onNext }: { onNext: () => void }) => (
  <motion.div
    className="card"
    initial={{ opacity: 0, x: 300 }}
    animate={{ opacity: 1, x: 0 }}
    exit={{ opacity: 0, x: -300 }}
    transition={{ duration: 0.5 }}
  >
    <h1 className="card-title">Penny on Us!!</h1>
    <div className="card-image-container">
      <Image
        src="/imgs/penny.png"
        alt="Penny"
        width={250}
        height={250}
        className="card-image"
      />
    </div>
    <button
      onClick={onNext}
      className="card-button"
    >
      Complete WISH
    </button>
  </motion.div>
);

const WishSentScreen = () => (
  <motion.div
    className="card"
    initial={{ opacity: 0, x: 300 }}
    animate={{ opacity: 1, x: 0 }}
    exit={{ opacity: 0, x: -300 }}
    transition={{ duration: 0.5 }}
  >
    <h1 className="card-title">Wish Sent...</h1>
    <div className="card-image-container">
      <Image
        src="/imgs/wish.png"
        alt="Wish"
        width={250}
        height={250}
        className="card-image"
      />
    </div>
    <p className="card-text">Good Luck!!</p>
  </motion.div>
);

export default function Home() {
  const [currentScreen, setCurrentScreen] = useState(0);

  const handleNext = () => {
    setCurrentScreen((prev) => prev + 1);
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-[#005596] p-4">
      <div className="w-full max-w-md relative overflow-hidden">
        <AnimatePresence mode="wait">
          {currentScreen === 0 && <PromotionScreen key="promotion" onNext={handleNext} />}
          {currentScreen === 1 && <LocationScreen key="location" onNext={handleNext} />}
          {currentScreen === 2 && <PaymentScreen key="payment" onNext={handleNext} />}
          {currentScreen === 3 && <WishSentScreen key="wish-sent" />}
        </AnimatePresence>
      </div>
    </div>
  );
}

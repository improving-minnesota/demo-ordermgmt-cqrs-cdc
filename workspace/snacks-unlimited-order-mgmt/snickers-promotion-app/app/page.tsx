"use client";

import { useState } from "react";
import { AnimatePresence } from "framer-motion";

// Import UI components
import ErrorAlert from "./components/ui/ErrorAlert";

// Import screen components
import PromotionScreen from "./components/screens/PromotionScreen";
import LocationScreen from "./components/screens/LocationScreen";
import PaymentScreen from "./components/screens/PaymentScreen";
import WishSentScreen from "./components/screens/WishSentScreen";

export default function Home() {
  const [currentScreen, setCurrentScreen] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [userName, setUserName] = useState("");
  const [orderId, setOrderId] = useState("");

  const callApi = async (endpoint: string, requestData = {}) => {
    setIsLoading(true);
    try {
      const response = await fetch(`/api/${endpoint}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData),
      });

      const responseData = await response.json();
      
      if (!response.ok || !responseData.success) {
        throw new Error(responseData.message || 'Something went wrong');
      }
      
      // Store orderId if it exists in the response
      if (endpoint === 'promotion' && responseData.data && responseData.data.orderId) {
        console.log('Order ID received:', responseData.data.orderId);
        setOrderId(responseData.data.orderId);
      }
      
      // If successful, move to next screen
      setCurrentScreen((prev) => prev + 1);
    } catch (err) {
      console.error(`API Error (${endpoint}):`, err);
      setError(err instanceof Error ? err.message : 'An unexpected error occurred');
    } finally {
      setIsLoading(false);
    }
  };

  const handlePromotionNext = () => {
    const promotionData = {
      orderId: "",
      items: [
        {
          itemId: "1",
          itemName: "Snickers",
          price: 0.01,
          quantity: 1
        }
      ]
    };
    callApi('promotion', promotionData);
  };

  const handleLocationNext = () => {
    // Only proceed if userName is not empty
    if (userName.trim()) {
      // Format the location data according to the API requirements
      const locationData = {
        orderId: orderId,
        customerName: userName,
        customerAddress: "2115 Summit Ave.",
        zipCode: "55105",
        latitude: 44.9374829167,
        longitude: -93.1893875758
      };
      
      callApi('location', locationData);
    }
  };

  const handlePaymentNext = () => {
    // Format the payment data according to the API requirements
    const paymentData = {
      orderId: orderId,
      paymentType: "Credit Card",
      creditCardType: "Magical Visa",
      creditCardNumber: "7777-7777-7777-7777",
      amount: 0.01
    };
    
    callApi('payment', paymentData);
  };

  const clearError = () => {
    setError(null);
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-[#005596] p-4">
      {error && <ErrorAlert message={error} onClose={clearError} />}
      
      <div className="w-full max-w-md relative overflow-hidden">
        <AnimatePresence mode="wait">
          {currentScreen === 0 && (
            <PromotionScreen key="promotion" onNext={handlePromotionNext} isLoading={isLoading} />
          )}
          {currentScreen === 1 && (
            <LocationScreen 
              key="location" 
              onNext={handleLocationNext} 
              isLoading={isLoading} 
              userName={userName}
              setUserName={setUserName}
            />
          )}
          {currentScreen === 2 && (
            <PaymentScreen key="payment" onNext={handlePaymentNext} isLoading={isLoading} />
          )}
          {currentScreen === 3 && <WishSentScreen key="wish-sent" />}
        </AnimatePresence>
      </div>
    </div>
  );
}

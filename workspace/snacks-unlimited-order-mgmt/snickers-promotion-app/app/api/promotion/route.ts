import { NextResponse } from 'next/server';
import { apiConfig, mockData, isDevelopment } from '../../config';

// Construct the full remote API URL from the configuration
const REMOTE_API_URL = `${apiConfig.remoteApiBaseUrl}${apiConfig.endpoints.itemDetails}`;

export async function POST(request: Request) {
  try {
    // Parse the incoming request
    const requestData = await request.json();
    
    // Log the request details
    console.log(`Promotion API request (${isDevelopment ? 'DEV' : 'PROD'} mode)`);
    console.log('Request data:', JSON.stringify(requestData, null, 2));
    
    // Use mock data in development mode if configured
    if (apiConfig.useMockData) {
      console.log('Using mock data for promotion request (development mode)');
      
      // Simulate network delay
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // Return mock response
      return NextResponse.json(mockData.promotion);
    }
    
    // In production mode or when mock is disabled, make the actual API call
    console.log('Proxying promotion request to:', REMOTE_API_URL);
    
    // Forward the request to the remote API with timeout
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), apiConfig.timeoutMs);
    
    try {
      const response = await fetch(REMOTE_API_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData),
        signal: controller.signal,
      });
      
      clearTimeout(timeoutId);
      
      // Parse the remote API response
      const responseData = await response.json();
      
      // Check if the response was successful
      if (!response.ok) {
        throw new Error(responseData.message || 'Remote API request failed');
      }
      
      // Return the remote API response to the client
      return NextResponse.json({
        success: true,
        message: 'Promotion request processed successfully',
        data: responseData,
      });
    } catch (error) {
      // Handle fetch-specific errors
      const fetchError = error as { name?: string };
      if (fetchError.name === 'AbortError') {
        throw new Error(`Request to ${REMOTE_API_URL} timed out after ${apiConfig.timeoutMs}ms`);
      }
      throw error;
    }
  } catch (error) {
    console.error('Promotion API error:', error);
    return NextResponse.json(
      { 
        success: false, 
        message: error instanceof Error ? error.message : 'Failed to process promotion request'
      },
      { status: 500 }
    );
  }
}

/*
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const { Contract } = require('fabric-contract-api');

class EMS extends Contract {

    async initLedger(ctx) {
        console.info('============= START : Initialize Ledger ===========');
        const state = {
            docType: 'emsstate',
            powerProviderPrice: '1200',
            powerProviderAmount: '500',
            powerProviderBalanceEur: '10000000',
            powerPlantPrice: '700',
            powerPlantAmount: '50000',
            powerPlantBalanceEur: '20000'
        }

        await ctx.stub.putState('state', Buffer.from(JSON.stringify(state)));

        console.info('============= END : Initialize Ledger ===========');
    }

    async queryState(ctx) {
        console.info('============= START : queryState ===========');
        const stateAsBytes = await ctx.stub.getState('state');

        console.info('============= END : queryState ===========');
        return stateAsBytes.toString();
    }

    async changeBulkPrice(ctx, newPrice) {
        console.info('============= START : changeBulkPrice ===========');
        const stateB = await ctx.stub.getState('state');
        const state = JSON.parse(stateB.toString());
        state.powerPlantPrice = newPrice;
        await ctx.stub.putState('state', Buffer.from(JSON.stringify(state)))
        ctx.stub.setEvent("priceChanged", Buffer.from(newPrice));
        console.info('============= END : changeBulkPrice ===========');
    }

    async changeRetailPrice(ctx, newPrice) {
        console.info('============= START : changeRetailPrice ===========');
        const stateB = await ctx.stub.getState('state');
        const state = JSON.parse(stateB.toString());
        state.powerProviderPrice = newPrice;
        await ctx.stub.putState('state', Buffer.from(JSON.stringify(state)));
        console.info('============= END : changeRetailPrice ===========');
    }

    async buyInBulk(ctx, amount) {
        console.info('============= START : buyInBulk ===========');
        const amountInt = parseInt(amount);
        const stateB = await ctx.stub.getState('state');
        const state = JSON.parse(stateB.toString());
        const priceInt = parseInt(state.powerPlantPrice);
        state.powerProviderAmount = (parseInt(state.powerProviderAmount) + amountInt).toString();
        state.powerPlantAmount = (parseInt(state.powerPlantAmount) - amountInt).toString();
        state.powerProviderBalanceEur = (parseInt(state.powerProviderBalanceEur) - (amountInt * priceInt)).toString();
        state.powerPlantBalanceEur = (parseInt(state.powerPlantBalanceEur) + (amountInt * priceInt)).toString();
        await ctx.stub.putState('state', Buffer.from(JSON.stringify(state)));
        console.info('============= END : buyInBulk ===========');
    }
}

module.exports = EMS;

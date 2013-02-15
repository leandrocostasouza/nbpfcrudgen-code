function handleSubmit(xhr, status, args, dialog) {
    if(args.validationFailed) {
        dialog.effect('shake', { times:3 }, 100);
    } else {
        dialog.hide();
    }
}